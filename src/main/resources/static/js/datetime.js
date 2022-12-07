let store = document.getElementById("storeStatistic");
let year = document.getElementById("yearStatistic");
let month = document.getElementById("monthStatistic");
let day = document.getElementById("dayStatistic");
let btnStatistic = document.getElementById("btnStatistic");

if (year && month && day && btnStatistic) {
  const getYearsInInvoice = () => {
    let options = ``;
    options += `<option value="" selected disabled>Năm</option>
        <option value="2022">2022</option>
        <option value="2021">2021</option>
        <option value="2020">2020</option>
        <option value="2019">2019</option>
        <option value="2018">2018</option>
        <option value="2017">2017</option>
        <option value="2016">2016</option>
        <option value="2015">2015</option>
        <option value="2014">2014</option>
        <option value="2013">2013</option>
        <option value="2012">2012</option>
        <option value="2011">2011</option>`;
    year.innerHTML = options;
  };

  const handlePickDate = () => {
    year.onchange = () => {
      month.removeAttribute("disabled");
    };
    month.onchange = () => {
      let options = `<option value="">Ngày</option>`;
      for (let i = 1; i < getDayByMonthAndYear(month.value, year.value); i++) {
        options += `<option value="${i}">Ngày ${i}</option>`;
      }
      day.innerHTML = options;
    };
  };

  const getDayByMonthAndYear = (month, year) => {
    return new Date(year, month, 0).getDate();
  };

  const handleSubmitStatistic = () => {
    btnStatistic.onclick = () => {
      let storeId = store.value;
      let yearVal = year.value;
      let monthVal = month.value;
      let dayVal = day.value;
      if (yearVal && storeId && monthVal && dayVal) {
        location.assign(
          `/statistic?store=${storeId}&year=${yearVal}&month=${monthVal}&day=${dayVal}`
        );
      }
      if (yearVal && storeId && monthVal && !dayVal) {
        location.assign(
          `/statistic?store=${storeId}&year=${yearVal}&month=${monthVal}`
        );
      }
      if (yearVal && storeId && !monthVal && !dayVal) {
        location.assign(`/statistic?store=${storeId}&year=${yearVal}`);
      }
    };
  };

  handlePickDate();
  handleSubmitStatistic();
  getYearsInInvoice();
}
