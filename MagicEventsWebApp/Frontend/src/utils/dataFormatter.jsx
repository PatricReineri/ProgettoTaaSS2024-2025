// Restituisce i prossimi N giorni (incluso oggi) formattati come { day: '2 Luglio' }
export function getNextNDaysFormatted(daysCount) {
    const monthNames = [
        'Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno',
        'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'
    ];
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return Array.from({ length: daysCount }, (_, i) => {
        const d = new Date(today);
        d.setDate(d.getDate() + i);
        return { day: `${d.getDate()} ${monthNames[d.getMonth()]}` };
    });
}

// Unisce giorni e eventi associandoli a tutti i giorni in cui l’evento dura
export function mergeDaysAndEvents(daysList, events) {
    const grouped = daysList.reduce((acc, { day }) => {
        acc[day] = [];
        return acc;
    }, {});

    const monthNames = [
        'Gennaio', 'Febbraio', 'Marzo', 'Aprile', 'Maggio', 'Giugno',
        'Luglio', 'Agosto', 'Settembre', 'Ottobre', 'Novembre', 'Dicembre'
    ];

    // Utility: restituisce tutti i giorni tra due date formattati come '2 Luglio'
    function getFormattedDateRange(start, end) {
        const range = [];
        const current = new Date(start);
        const endDate = new Date(end);
        current.setHours(0, 0, 0, 0);
        endDate.setHours(0, 0, 0, 0);

        while (current <= endDate) {
            const formatted = `${current.getDate()} ${monthNames[current.getMonth()]}`;
            range.push(formatted);
            current.setDate(current.getDate() + 1);
        }
        return range;
    }

    events.forEach((event) => {
        const startDay = event.starting.day; // es. '2025-07-02'
        const endDay = event.ending.day;     // es. '2025-07-04'

        const eventDays = getFormattedDateRange(startDay, endDay); // ['2 Luglio', '3 Luglio', '4 Luglio']

        eventDays.forEach((day) => {
            if (grouped[day]) {
                grouped[day].push(event);
            }
        });
    });

    return daysList.map(({ day }) => ({
        day,
        events: grouped[day],
    }));
}

// Filtra eventi nei prossimi N giorni e formatta starting/ending in { day, hour }
export function getUpcomingEvents(events, daysAhead) {
    const now = new Date();
    now.setHours(0, 0, 0, 0);
    const max = new Date(now);
    max.setDate(max.getDate() + daysAhead);

    return events
        .map((e) => {
            const startDate = new Date(e.starting);
            const endDate = new Date(e.ending);
            return {
                ...e,
                _startDate: startDate,
                _endDate: endDate,
            };
        })
        .filter((e) => e._startDate <= max && e._endDate >= now)
        .map((e) => {
            const start = e._startDate;
            const end = e._endDate;

            const pad = (n) => String(n).padStart(2, '0');

            return {
                ...e,
                starting: {
                    day: start.toISOString().split('T')[0], // 'YYYY-MM-DD'
                    hour: `${pad(start.getHours())}:${pad(start.getMinutes())}`,
                },
                ending: {
                    day: end.toISOString().split('T')[0],   // 'YYYY-MM-DD'
                },
            };
        })
        .map(({ _startDate, _endDate, ...e }) => e);
}

export function convertDayToISO(dayObj) {
    const now = new Date();

    const year = now.getFullYear();
    const hour = now.getHours();
    const minute = now.getMinutes();
    const second = now.getSeconds();

    const monthMap = {
        'Gennaio': 0,
        'Febbraio': 1,
        'Marzo': 2,
        'Aprile': 3,
        'Maggio': 4,
        'Giugno': 5,
        'Luglio': 6,
        'Agosto': 7,
        'Settembre': 8,
        'Ottobre': 9,
        'Novembre': 10,
        'Dicembre': 11,
    };

    const [dayStr, monthStr] = dayObj.day.split(' ');
    const day = parseInt(dayStr, 10);
    const month = monthMap[monthStr];

    const date = new Date(year, month, day, hour, minute, second);

    return date.toISOString().replace('.000Z', '');
}

