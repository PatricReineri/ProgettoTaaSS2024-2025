const Homepage = () => {
	return (
		<div className="h-full backgroundHome overflow-y-auto overscroll-none snap-y">
			<div className="snap-start p-2 sm:p-4">
				<div className="bg-black/30 h-20 sm:h-24 backdrop-blur-md p-3 sm:p-4 w-32 sm:w-40 text-[#E4DCEF] text-center rounded-md shadow-2xl">
					<span className="text-sm sm:text-base">Crea degli</span>
					<p className="!text-[#EE0E51] font-bold text-center text-sm sm:text-base">eventi</p>
					<span className="text-sm sm:text-base">magici!</span>
				</div>
				<div className="h-[calc(100vh-12rem)] sm:h-[calc(100vh-15rem)] w-20"></div>
			</div>

			<div className="snap-start pt-8 sm:pt-12 md:pt-20 relative w-full px-4 sm:px-8 md:px-16 bg-black/30 backdrop-blur-lg p-4 sm:p-6 md:p-8 rounded-t-4xl shadow-2xl">
				<p className="absolute top-2 sm:top-4 md:top-0 text-[#E4DCEF] left-1/2 transform -translate-x-1/2 md:left-[45%] md:transform-none font-extrabold text-lg sm:text-xl p-2">
					Scopri di più
				</p>
				
				{/* Mobile: vertical stack, Tablet: 2 columns, Desktop: 3 columns */}
				<div className="flex flex-col md:flex-row gap-4 sm:gap-6 md:gap-8 pt-8 sm:pt-12 md:pt-0">
					<div className="bg-[#363540] hover:shadow-xl hover:shadow-[#EE0E51] hover:scale-105 md:hover:scale-110 transition-all duration-300 text-[#E4DCEF] p-4 sm:p-6 flex-1 rounded-2xl border border-[#EE0E51]">
						<h1 className="text-lg sm:text-xl font-extrabold mb-2">Crea</h1>
						<p className="text-sm sm:text-base">
							Crea i tuoi eventi esclusivi, inviando il link o il QR code ai tuoi invitati potrà partecipare solo chi vuoi
						</p>
					</div>
					<div className="bg-[#363540] hover:shadow-xl hover:shadow-[#EE0E51] hover:scale-105 md:hover:scale-110 transition-all duration-300 text-[#E4DCEF] p-4 sm:p-6 flex-1 rounded-2xl border border-[#EE0E51]">
						<h1 className="text-lg sm:text-xl font-extrabold mb-2">Partecipa</h1>
						<p className="text-sm sm:text-base"> 
							Gestisci e partecipa ad eventi e divertiti
						</p>
					</div>
					<div className="bg-[#363540] hover:shadow-xl hover:shadow-[#EE0E51] hover:scale-105 md:hover:scale-110 transition-all duration-300 text-[#E4DCEF] p-4 sm:p-6 flex-1 rounded-2xl border border-[#EE0E51]">
						<h1 className="text-lg sm:text-xl font-extrabold mb-2">Interagisci</h1>
						<p className="text-sm sm:text-base">
							Interagisci con i tuoi invitati: sono disponibili diverse funzionalità che puoi abilitare per il tuo evento, provale tutte
						</p>
					</div>
				</div>
			</div>
		</div>
	);
};

export default Homepage;
