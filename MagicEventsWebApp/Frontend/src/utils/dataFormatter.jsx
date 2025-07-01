// Restituisce i prossimi N giorni (incluso oggi) formattati { day }
export function getNextNDaysFormatted(daysCount) {
    const monthNames = [
        'Gennaio',
        'Febbraio',
        'Marzo',
        'Aprile',
        'Maggio',
        'Giugno',
        'Luglio',
        'Agosto',
        'Settembre',
        'Ottobre',
        'Novembre',
        'Dicembre',
    ];
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return Array.from({ length: daysCount }, (_, i) => {
        const d = new Date(today);
        d.setDate(d.getDate() + i);
        return { day: `${d.getDate()} ${monthNames[d.getMonth()]}` };
    });
}

// Unisce giorni e eventi (con starting.day) in una struttura { day, events }
export function mergeDaysAndEvents(daysList, events) {
    const grouped = daysList.reduce((acc, { day }) => {
        acc[day] = [];
        return acc;
    }, {});
    events.forEach((e) => {
        const day = e.starting.day;
        if (grouped[day]) grouped[day].push(e);
    });
    return daysList.map(({ day }) => ({ day, events: grouped[day] }));
}

export function getUpcomingEvents(events, daysAhead) {
    const now = new Date();
    now.setHours(0, 0, 0, 0);
    const max = new Date(now);
    max.setDate(max.getDate() + daysAhead);

    const monthNames = [
        'Gennaio',
        'Febbraio',
        'Marzo',
        'Aprile',
        'Maggio',
        'Giugno',
        'Luglio',
        'Agosto',
        'Settembre',
        'Ottobre',
        'Novembre',
        'Dicembre',
    ];

    return events
        .map((e) => {
            const d = new Date(e.starting);
            return {
                ...e,
                _date: d,
            };
        })
        .filter((e) => e._date >= now && e._date <= max)
        .map((e) => {
            const d = e._date;
            const day = `${d.getDate()} ${monthNames[d.getMonth()]}`;
            const hh = String(d.getHours()).padStart(2, '0');
            const mm = String(d.getMinutes()).padStart(2, '0');
            return {
                ...e,
                starting: { day, hour: `${hh}:${mm}` },
                // tolgo il campo interno
            };
        })
        .map(({ _date, ...e }) => e);
}