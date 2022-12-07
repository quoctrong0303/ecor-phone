const headerResponsive = () => {
  let barsBtn = document.querySelector(".headerMenu");
  let wrapper = document.querySelector(".wrapperMenu");
  if (barsBtn && wrapper) {
    barsBtn.onclick = () => {
      wrapper.classList.toggle("hidden");
    };
  }
};

headerResponsive();
