const NavBar = ({ logo, children, actions }) => {
	return (
		<nav class=" bg-[#363540] text-[#E8F2FC] sticky top-0 h-14 ">
			<ul class="flex space-x-4 p-4 items-center ">
				{logo}
				{children}
				<div class="w-full"></div>
				{actions}
			</ul>
		</nav>
	);
};

export default NavBar;
