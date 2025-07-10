const Homepage = () => {
	return (
		<div className="h-full backgroundHome overflow-y-auto overscroll-none snap-y">
			<div className="snap-start  p-4">
				<div className=" bg-black/30 h-24 backdrop-blur-md p-4 w-40 text-[#E4DCEF]  text-center rounded-md shadow-2xl ">
					Crea degli <p className="!text-[#EE0E51] font-bold text-center">eventi</p> magici!
				</div>
				<div className=" h-[calc(100vh-15rem)]  w-20"></div>
			</div>

			<div className="snap-start pt-20 relative w-full px-16 bg-black/30 backdrop-blur-lg p-8 rounded-t-4xl shadow-2xl flex flex-row overflow-x-auto gap-8 snap-x justify-evenly  ">
				<p className="absolute top-0 text-[#E4DCEF] left-[45%] font-extrabold text-xl p-2">Scopri di più</p>
				<div className="bg-[#363540] hover:shadow-xl hover:shadow-[#EE0E51] hover:scale-110 snap-center min-w-[12rem] text-[#E4DCEF] p-4 flex-auto max-w-[15rem] rounded-2xl border border-[#EE0E51] ">
					<h1 className="text-xl font-extrabold mb-2">Crea</h1>
					<p>
						Crea i tuoi eventi esclusivi, inviando il link o il QR code ai tuoi invitati potrà partecipare solo chi vuoi
					</p>
				</div>
				<div className="bg-[#363540] hover:shadow-xl hover:shadow-[#EE0E51] hover:scale-110 snap-center min-w-[12rem] text-[#E4DCEF] p-4  flex-auto max-w-[15rem] rounded-2xl border border-[#EE0E51] ">
					<h1 className="text-xl font-extrabold mb-2">Partecipa</h1>
					<p> 
						Gestisci e partecipa ad eventi e divertiti
					</p>
				</div>
				<div className="bg-[#363540] hover:shadow-xl hover:shadow-[#EE0E51] hover:scale-110 snap-center min-w-[12rem] text-[#E4DCEF] p-4 flex-auto max-w-[15rem] rounded-2xl border border-[#EE0E51] ">
					<h1 className="text-xl font-extrabold mb-2">Interagisci</h1>
					<p>
						Interagisci con i tuoi invitati: sono disponibili diverse funzionalità che puoi abilitare per il tuo evento, provale tutte
					</p>
				</div>
			</div>
		</div>
	);
};

export default Homepage;
