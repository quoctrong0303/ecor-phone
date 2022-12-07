const orderChart = () => {
  const element = document.getElementById("orderChart");
  const orderLabels = ["Đang chờ", "Đã huỷ", "Đã duyệt", "Giao thành công"];

  const orderData = {
    labels: orderLabels,
    datasets: [
      {
        backgroundColor: ["red", "blue", "pink", "yellow"],
        borderColor: "rgb(255, 99, 132)",
        data: [7, 10, 5, 2],
      },
    ],
  };

  const config = {
    type: "doughnut",
    data: orderData,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        title: {
          display: true,
          text: "Đơn đặt",
        },
      },
    },
  };

  if (element) {
	const orderChart = new Chart(element, config);
}
};

const productChart = () => {
  const element = document.getElementById("productChart");
  const productLabels = ["Đang chờ", "Đã huỷ", "Đã duyệt", "Giao thành công"];

  const productData = {
    labels: productLabels,
    datasets: [
      {
        backgroundColor: ["red", "blue", "pink", "yellow"],
        borderColor: "rgb(255, 99, 132)",
        data: [7, 10, 5, 2],
      },
    ],
  };

  const config = {
    type: "doughnut",
    data: productData,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        title: {
          display: true,
          text: "Sản phẩm bán chạy",
        },
      },
    },
  };

  if (productChart) {
	const productChart = new Chart(
    element,
    config
  );
}
};

const incomeChart = () => {
	const element = document.getElementById("incomeChart");
  const incomeLabels = ["Đang chờ", "Đã huỷ", "Đã duyệt", "Giao thành công"];

  const incomeData = {
    labels: incomeLabels,
    datasets: [
      {
        backgroundColor: ["red", "blue", "pink", "yellow"],
        borderColor: "rgb(255, 99, 132)",
        data: [7, 10, 5, 2],
      },
    ],
  };

  const config = {
    type: "doughnut",
    data: incomeData,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        title: {
          display: true,
          text: "Doanh thu",
        },
      },
    },
  };

  if (incomeChart) {
	const incomeChart = new Chart(element, config);
}
};

if (document.getElementById("incomeChart") && document.getElementById("productChart") && document.getElementById("orderChart")) {
	orderChart();
	productChart();
	incomeChart();
}


