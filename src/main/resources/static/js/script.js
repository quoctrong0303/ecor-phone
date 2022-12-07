const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);
var scrollButton = $(".arrow-scroll");
var prefile = "";
const app = {
	scrollButton: () => {
		var body = document.body,
			html = document.documentElement;

		var height = Math.max(
			body.scrollHeight,
			body.offsetHeight,
			html.clientHeight,
			html.scrollHeight,
			html.offsetHeight
		);
		isToggle = false;
		function toggleList() {
			var lists = $$(".toggle-list");
			lists.forEach((element) => {
				element.onclick = () => {
					isToggle = !isToggle;
					if (isToggle) {
						element.classList.add("show");
						element.classList.remove("hide");
					} else {
						element.classList.remove("show");
						element.classList.add("hide");
					}
				};
			});
		}
		toggleList();

		window.onscroll = () => {
			if (
				document.body.scrollTop > height / 4 ||
				document.documentElement.scrollTop > height / 4
			) {
				scrollButton.style.display = "block";
			} else {
				scrollButton.style.display = "none";
			}
		};

		scrollButton.onclick = () => {
			document.body.scrollTop = 0;
			document.documentElement.scrollTop = 0;
		};
	},
	showLoadingButton: (formSelector) => {
		let form = $(formSelector);
		let btnElement = form.querySelector(".button");
		let btnElementLoading = form.querySelector(".loading");
		btnElement.style.display = "none";
		btnElementLoading.style.display = "inline-flex";
	},
	hideLoadingButton: (formSelector) => {
		let form = $(formSelector);
		let btnElement = form.querySelector(".button");
		let btnElementLoading = form.querySelector(".loading");
		btnElement.style.display = "block";
		btnElementLoading.style.display = "none";
	},
	handleFocus: (selector, oldClass, newClass) => {
		let Elements = $$(selector);
		if (Elements) {
			Elements.forEach((Element) => {
				// Nếu như click vào element
				Element.onclick = (e) => {
					e.preventDefault();
					// render lại classlist normal
					Elements.forEach((Element) => {
						Element.setAttribute("class", oldClass);
					});
					// Set các class cho element đã click là active
					e.target.closest(selector).setAttribute("class", newClass);
				};
			});
		}
	},
	handleLoadMoreContent: (contentSelector, buttonSelector, minHeight) => {
		let contentElement = $(contentSelector);
		let classMinHeight = "h-[" + minHeight + "]";
		// let classFullHeight = "h-full";
		let btnElement = $(buttonSelector);
		if (btnElement) {
			btnElement.onclick = (e) => {
				//Nếu như thẻ chứa content đang ở chế độ thu nhỏ
				if (contentElement.classList.contains(classMinHeight)) {
					e.preventDefault();
					contentElement.classList.remove(classMinHeight);
					btnElement.innerHTML = `Thu gọn <i class="fas fa-chevron-up"></i>`;
					// Ngược lại nếu content ở chế độ đọc full
				} else {
					e.preventDefault();
					contentElement.classList.add(classMinHeight);
					btnElement.innerHTML = `Xem thêm <i class="fas fa-chevron-down"></i>`;
				}
			};
		}
	},
	toggleModal: (modalSelector, btnSelector) => {
		let btnElements = $$(btnSelector);
		let modalElement = $(modalSelector);
		btnElements.forEach((element) => {
			element.onclick = (e) => {
				e.preventDefault();
				// Ẩn hiện modal bằng cách thay đổi class
				modalElement.classList.toggle("hidden");
				modalElement.classList.toggle("fixed");
				modalElement.classList.toggle("flex");
			};
		});
	},
	removeItemCart: async (e) => {

		let id = e.target.closest("section").dataset.id;
		if (id) {
			let response = await fetch("/cart/" + id, {
				method: "DELETE"
			})
			let data = await response.json();
			console.log(data);
			new window.FlashMessage(data.message, data.status, {
				progress: true,
				interactive: true,
				timeout: 5000,
				appear_delay: 200,
			});
			if (data.status == "success") {
				let element = e.target.closest(".item-cart");
				let total = document.getElementById("total-price-cart");
				if (total) total.innerText = data.data;
				let cart_count = $("#cart-count");
				if (element) {
					element.remove();
					cart_count.textContent--;
				}
			}
		}


	},
	handleCountInput: (inputElement, minusBtnElement, plusBtnElement) => {
		let input = $$(inputElement);
		let minus = $$(minusBtnElement);
		let plus = $$(plusBtnElement);
		if (input && minus && plus) {
			minus.forEach((element) => {
				element.onclick = async () => {
					//khi nhấn trừ quantity
					element.parentElement.querySelector(inputElement).value--;
					let id = element.closest("section").dataset.id;
					//lấy số lượng ở ô input
					let quantity = element.parentElement.querySelector(inputElement).value;
					formData = new FormData();
					formData.append("quantity", parseInt(quantity));
					// Ajax...
					let response = await fetch("/cart/" + id, {
						method: "PUT",
						body: formData
					})
					let data = await response.json();
					new window.FlashMessage(data.message, data.status, {
						progress: true,
						interactive: true,
						timeout: 5000,
						appear_delay: 200,
					});
					console.log(data);
					if (data.status == "success") {
						let price = element.closest("section").querySelector(".cartItemPrice");
						let totalPrice = document.querySelector("#total-price-cart");
						let priceHasDiscount = element.closest("section").querySelector(".cartItemPriceHasDiscount");
						if (price) {
							//data.data vì mình đặt trùng tên, không sao
							price.innerText = data.data.formatedPrice;
						}
						if (totalPrice) {
							//data.data vì mình đặt trùng tên, không sao
							totalPrice.innerText = data.data.formatedTotalPrice;
						}
						if (priceHasDiscount) {
							priceHasDiscount.innerText = data.data.formatedPriceHasDiscount;
						}
					} else {
						element.parentElement.querySelector(inputElement).value++;
					}
				};
			});
			plus.forEach((element) => {
				element.onclick = async () => {
					//khi nhấn cộng quantity 
					element.parentElement.querySelector(inputElement).value++;
					let id = element.closest("section").dataset.id;
					//lấy số lượng ở ô input
					let quantity = element.parentElement.querySelector(inputElement).value;
					formData = new FormData();
					formData.append("quantity", parseInt(quantity));
					// Ajax...
					let response = await fetch("/cart/" + id, {
						method: "PUT",
						body: formData
					})
					let data = await response.json();
					new window.FlashMessage(data.message, data.status, {
						progress: true,
						interactive: true,
						timeout: 5000,
						appear_delay: 200,
					});
					console.log(data);
					if (data.status == "success") {
						let price = element.closest("section").querySelector(".cartItemPrice");
						let totalPrice = document.querySelector("#total-price-cart");
						let priceHasDiscount = element.closest("section").querySelector(".cartItemPriceHasDiscount");
						if (price) {
							//data.data vì mình đặt trùng tên, không sao
							price.innerText = data.data.formatedPrice;
						}
						if (totalPrice) {
							//data.data vì mình đặt trùng tên, không sao
							totalPrice.innerText = data.data.formatedTotalPrice;
						}
						if (priceHasDiscount) {
							priceHasDiscount.innerText = data.data.formatedPriceHasDiscount;
						}
					} else {
						element.parentElement.querySelector(inputElement).value--;
					}
				};
			});
		}
	},
	fetchProvinces: (provinceSelector, districtSelector, wardSelector) => {
		let province = document.querySelector(provinceSelector);
		let district = document.querySelector(districtSelector);
		let ward = document.querySelector(wardSelector);
		let url = "https://vapi.vnappmob.com";
		if (province && district && ward) {
			//   Lấy tất cả tỉnh/ thành trên VN
			fetch(url + "/api/province")
				.then((response) => response.json())
				.then((data) => {
					//   console.log(data);
					let html = `<option name="" id="" disabled selected>Tỉnh/thành</option>`;
					data.results.forEach((data) => {
						html += `<option value="${data.province_id}">${data.province_name}</option>`;
					});
					province.innerHTML = html;
					//   console.log(html);
				});

			// Khi select tỉnh thành, thì dựa vào value để lấy ds quận huyện
			province.onchange = (e) => {
				let html = `<option name="" id="" disabled selected>Quận/huyện</option>`;
				fetch(url + `/api/province/district/${e.target.value}`)
					.then((response) => response.json())
					.then((data) => {
						data.results.forEach((data) => {
							html += `<option value="${data.district_id}">${data.district_name}</option>`;
						});
						// console.log(data);
						// console.log(html);
						district.innerHTML = html;
						// Reset lại các phường xã khi chưa chọn quận /huyện
						ward.innerHTML = `<option name="" id="" disabled selected>Phường/xã</option>`;
					});
			};

			//   Khi select quận huyện dựa vào value lấy phường/ xã
			district.onchange = (e) => {
				let html = `<option name="" id="" disabled selected>Phường/xã</option>`;
				fetch(url + `/api/province/ward/${e.target.value}`)
					.then((response) => response.json())
					.then((data) => {
						data.results.forEach((data) => {
							html += `<option value="${data.ward_id}">${data.ward_name}</option>`;
						});
						// console.log(data);
						// console.log(html);
						ward.innerHTML = html;
					});
			};
		}
	},
	handleCheckout: (formSelector, btnCheckout) => {
		formElement = $(formSelector);
		btnElement = $(btnCheckout);
		if (formElement && btnElement) {
			// Khi dã chọn pphường xã thì mới bật nút đặt hàng
			console.log(formElement.querySelector("#checkoutWard"));
			formElement.querySelector("#checkoutWard").onchange = () => {
				app.toggleBtn(
					".button",
					"btn-primary",
					"btn-disabled",
					"#checkoutWard"
				);
			};
			// Khi nhấn vào nút đặt hàng
			btnElement.onclick = () => {
				let data = {};
				data.fullname = formElement.querySelector(
					"input[name='fullname']"
				).value;
				data.email = formElement.querySelector("input[name='email']").value;
				data.phone = formElement.querySelector("input[name='phone']").value;
				// option:checked để lấy thông tin element của option đang được check
				data.address = formElement.querySelector(
					"textarea[name='address']"
				).value;
				data.address +=
					" " +
					formElement.querySelector("select[name='ward'] option:checked")
						.innerHTML;
				data.address +=
					" " +
					formElement.querySelector("select[name='district'] option:checked")
						.innerHTML;
				data.address +=
					" " +
					formElement.querySelector("select[name='province'] option:checked")
						.innerHTML;
				// Dữ liệu được gửi đi, khi nào làm backend thì lấy dữ liẹu sản phẩm trong giỏ từ cart session
				console.log(data);
			};
		}
	},
	toggleBtn: (btnSelector, classEnableBtn, classDisableBtn, dataSelector) => {
		let dataElement = $(dataSelector);
		let btnElement = $(btnSelector);
		if (dataElement && btnElement) {
			// Ở đây làm thêm bước xét tên của tag name nữa
			if (dataElement.tagName == "SELECT") {
				if (
					// Nếu như option có select đang được chọn tồn tại attribute disabled thì không enable button
					dataElement
						.querySelector("option:checked")
						.hasAttribute("disabled") == false
				) {
					btnElement.removeAttribute("disabled");
					btnElement.classList.add(classEnableBtn);
					btnElement.classList.remove(classDisableBtn);
				} else {
					btnElement.setAttribute("disabled");
					btnElement.classList.remove(classEnableBtn);
					btnElement.classList.add(classDisableBtn);
				}
			}
		}
	},
	toggleDisabledForm: (btnSelector, formSelector) => {
		let btnElement = $(btnSelector);
		let btnUpdatingElement = $(".btnUpdating");
		let btnUpdate = $(".btnUpdate");
		let editElement = $(".button-edit");
		let removeElement = $(".button-remove");
		let formElement = $(formSelector);
		if (btnElement && formElement) {
			btnElement.onclick = (e) => {
				e.preventDefault();
				// Cập nhật lại button
				btnUpdate.classList.toggle("hidden");
				btnUpdatingElement.classList.toggle("hidden");
				editElement.classList.toggle("hidden");
				removeElement.classList.toggle("hidden");

				let inputs = formElement.querySelectorAll("input");
				let textareas = formElement.querySelectorAll("textarea");
				// Xoá attribute disabled khỏi tất cả input trong form khi
				// nút cập nhật thông tin được click
				inputs.forEach((input) => {
					// Không thẻ sửa field username và email
					input.getAttribute("name") == "username" ||
						input.getAttribute("name") == "email"
						? ""
						: input.removeAttribute("disabled");
				});

				textareas.forEach((input) => {
					input.removeAttribute("disabled");
				});
			};
		}
	},
	handleSaveProfile: () => {
		let editElement = $(".button-edit");
		let removeElement = $(".button-remove");
		let btnSave = $("#btnSave");
		let formSubmit = $("#fProfile");
		let btnUpdate = $(".btnUpdate");
		let btnUpdating = $(".btnUpdating");
		let btnCancel = $("#btnCancel");
		if (btnSave && btnCancel) {
			btnSave.onclick = (e) => {
				let data = {};
				//e.preventDefault();
				// Cập nhật lại button
				btnUpdate.classList.toggle("hidden");
				btnUpdating.classList.toggle("hidden");
				editElement.classList.toggle("hidden");
				removeElement.classList.toggle("hidden");
				// Gán dữ liệu vào object data
				data.fullname = formSubmit.querySelector("input[name = 'fullname']")
					? formSubmit.querySelector("input[name = 'fullname']").value.trim()
					: null;

				data.numberphone = formSubmit.querySelector(
					"input[name = 'numberphone']"
				)
					? formSubmit.querySelector("input[name = 'numberphone']").value.trim()
					: null;

				data.address = formSubmit.querySelector("textarea[name = 'address']")
					? formSubmit.querySelector("textarea[name = 'address']").value
					: null;

				data.avatar = formSubmit.querySelector("input[name = 'avatar']")
					? formSubmit.querySelector("input[name = 'avatar']").files[0]
					: null;

				console.log(data);
			};
			// Khi click vào nút cancel
			btnCancel.onclick = (e) => {
				e.preventDefault();
				formSubmit.reset();
				// Cập nhật lại button
				btnUpdate.classList.toggle("hidden");
				btnUpdating.classList.toggle("hidden");
				editElement.classList.toggle("hidden");
				removeElement.classList.toggle("hidden");
				formSubmit.querySelector("input[name = 'fullname']").setAttribute("disabled", "disabled");
				formSubmit.querySelector("textarea[name = 'address']").setAttribute("disabled", "disabled");
				formSubmit.querySelector("input[name = 'email']").setAttribute("disabled", "disabled");
			};
		}
	},
	handleEditAavatar: (btnEdit, btnRemove, inputSelector, imgSelector) => {
		let editElement = $(btnEdit);
		let removeElement = $(btnRemove);
		let inputElement = $(inputSelector);
		if (editElement && removeElement && inputElement) {
			// Khi nút remove avatar được click
			app.previewImg(inputSelector, imgSelector, btnEdit);
			removeElement.onclick = (e) => {
				// Call API xoá avatar của user
				// ...
				app.previewImg(inputSelector, imgSelector, btnEdit);
				// Thay đổi hình ảnh lại mặc định
				$(imgSelector).setAttribute(
					"src",
					"/file/placeholderAvatar.png"
				);
				// Xoá file hình ảnh khỏi file input
				inputElement.value = "";
				e.preventDefault();
			};
		}
	},
	previewImg: (inputSelector, ImgSelector, btnSelectImg) => {
		let fileImg = $(inputSelector);
		let imgElement = $(ImgSelector);
		let editElement = $(btnSelectImg);
		let url;
		let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i; //các tập tin cho phép
		// Khi nút edit avatar được click
		if (fileImg && imgElement && editElement) {
			editElement.onclick = (e) => {
				e.preventDefault();
				fileImg.click();
				// Chạy hàm này để preview image khi upload file
				if (fileImg) {
					fileImg.onchange = (e) => {
						if (prefile != "") {
							// console.log("xoa ne");
							URL.revokeObjectURL(prefile);
						}
						let file = e.target.files[0];
						if (allowedExtensions.exec(file.name)) {
							url = URL.createObjectURL(file);
						} else {
							url = `/file/invalidImg.png`;
						}
						prefile = url;
						imgElement.setAttribute("src", url);
						// console.log(url);
					};
				}
			};
		}
	},
	// Xử lý lấy dữ liệu từ form contact, ở thư viện validator
	// đã xây dựng chức năng lấy data
	// Nhưng vì là thư viện chung nên sẽ validate dùng cho nhiều mục đích
	// Nên không thể viết phần call API vào thư viện validator
	// Phải xử lý riêng như hàm bên dưới
	// Những hàm lấy data viết trong file này đa số là
	// lấy biến cứng, không có tham số truyền vào
	// Khi có thay đổi class thì hãy vào đây thay đổi luôn
	handleSendContact: () => {
		let btnSend = $("#btnSendContact");
		let formElement = $("#fContact");
		if (btnSend && formElement) {
			let data = {};
			btnSend.onclick = () => {
				formElement.querySelector("input[name='fullname']")
					? (data.fullname = formElement.querySelector(
						"input[name='fullname']"
					).value)
					: null;
				formElement.querySelector("input[name='email']")
					? (data.email = formElement.querySelector(
						"input[name='email']"
					).value)
					: null;
				formElement.querySelector("input[name='numberphone']")
					? (data.numberphone = formElement.querySelector(
						"input[name='numberphone']"
					).value)
					: null;
				formElement.querySelector("textarea[name='content']")
					? (data.content = formElement.querySelector(
						"textarea[name='content']"
					).value)
					: null;
				console.log(data);
			};
		}
	},
	// <!-- Handle animation ẩn hiện nội dung của product detail khi click vào -->
	showTest: (e) => {
		e.preventDefault();
		let parentDiv = e.target.closest("div").parentElement;
		let formElement;
		if (parentDiv) {
			formElement = parentDiv.querySelector("form");
		}
		let iconElement = parentDiv.querySelector(".iconShowHide");
		let minusClass = `iconShowHide fas fa-minus-circle`;
		let plusClass = `iconShowHide fas fa-plus-circle`;
		if (formElement) {
			// Form đang hiện
			if (formElement.classList.contains("upHeight")) {
				formElement.classList.remove("block");
				formElement.classList.remove("upHeight");
				// Đổi icon - thành +
				iconElement.setAttribute("class", plusClass);
				setTimeout(() => {
					formElement.classList.add("hidden");
				}, 200);
				formElement.classList.add("reduceHeight");
				// Form đã ẩn
			} else {
				formElement.classList.remove("reduceHeight");
				formElement.classList.remove("hidden");
				formElement.classList.add("block");
				formElement.classList.add("upHeight");
				// Đổi icon + thành -
				iconElement.setAttribute("class", minusClass);
			}
		}
	},
	handeRemoveProduct: () => {
		let btnRemove = $$(".removeProduct");
		if (btnRemove) {
			btnRemove.forEach((element) => {
				element.onclick = (e) => {
					let id = e.target.dataset.id;
					//console.log(e.target);
					let nameProduct = element
						.closest("tr")
						.querySelector(".nameProduct")
						.textContent.trim();
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
            <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
              <!-- Header -->
              <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                  <h3 class="text-white uppercase font-bold">Xác nhận</h3>

              </div>
              <!-- Content -->
              <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                  <div class="p-5">
                    <p class="font-bold">Bạn chắc chắn muốn xoá ${nameProduct}?</p>
                  </div>
              </div>
              <!-- Footer -->
              <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                  <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                  <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
              </div>
            </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							app.handleDeleteAPI("/admin/product/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("tr").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("tr").remove();
									}, 300);
								} else {
									//khi response.status != success
								}
							});
							divElement.remove();
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				};
			});
		}
	},
	//Delete product bằng callAPI
	handleDeleteAPI: async (url, id) => {
		const fullUrl = url + id;
		const response = await fetch(fullUrl, {
			method: "DELETE",
		});
		return response.json();
	},
	handleSaveAPI: async (url, id, data) => {
		const fullUrl = url + id;
		const response = await fetch(fullUrl, {
			method: "PUT",
			body: data,
		});
		return response.json();
	},
	handleCreateAPI: async (url, data) => {//API return về response
		const fullUrl = url;
		const response = await fetch(fullUrl, {
			method: "POST",
			body: data,
		});
		return response.json();
	},
	arrImg: [],
	imgFormData: new FormData(),
	handleUploadMultipleFilesProduct() {
		let btn = $("#btnAddDetailsImg");
		let input = $("#detailImgsProduct");
		let table = $("#listImgDetails");
		let btnSubmit = $("#submitAddProduct");

		// $/i là ignore case là không xét chữ hoa, chữ thường
		let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i; //các tập tin cho phép
		if (btn && input && table) {
			btn.onclick = () => {
				input.click();
			};
			input.onchange = (e) => {
				let html = "";
				// Xoá lỗi khi onchange input
				let errorElement = $("#sumaryErrorsUploadFiles");
				if (errorElement) {
					errorElement.textContent = "";
				}
				for (var i = 0; i < e.target.files.length; i++) {
					// Khi không đúng dịnh dạng file ảnh
					if (!allowedExtensions.exec(e.target.files[i].name)) {
						if (errorElement) {
							errorElement.textContent = "Lỗi không đúng định dạng ảnh!";
							return;
						}
					}
					// Khi đúng định dạng file ảnh
					// Xử lý thêm img vào formData
					let isExist = false;
					// Kiểm tra xem file đã có trong mảng chưa
					app.arrImg.forEach((value, index) => {
						if (value.name == e.target.files[i].name) {
							isExist = true;
						}
					});
					// Nếu đã có thì bỏ qua các câu lên bên dưới và thực hiện i++
					if (isExist) {
						errorElement.textContent = `File ${e.target.files[i].name} đã tồn tại!`;
						continue;
					}
					// Them file vào mảng
					app.arrImg.push(e.target.files[i]);
					// console.log(app.arrImg);
				}
				// Render ảnh ra màn hình
				app.arrImg.forEach((value, index) => {
					let url = URL.createObjectURL(value);

					html += `
          <tr class="border-b-[1px] border-solid border-[#aaa]">
          <td class="align-middle">
              <img class="w-[80px] object-contain p-2" src="${url}" alt="">
          </td>
          <td class="align-middle">
              <div class="p-1">
                  <span>${value.name}</span>
                  
              </div>
          </td>
          <td class="align-middle text-right">
              <div class=" p-1">
                  <span  title="Xoá" class="btnRemoveImgsProduct hover:cursor-pointer hover:text-red-500"><i data-id="${index}" onclick="app.removeImagesProduct(event)" class="fas fa-trash"></i></span>
              
              </div>
          </td>
          </tr>
          `;
				});
				table.innerHTML = html;

				console.log(app.arrImg);
			};
		}
	},
	removeImagesProduct: (e) => {
		let index = e.target.dataset.id;
		let tr = e.target.closest("tr");
		if (tr && index) {
			tr.classList.add("fadeOut");
			app.arrImg.splice(index, 1);
			setTimeout(() => {
				tr.remove();
			}, 300);
		}
	},
	addProduct: async () => {
		//Mỗi khi click thêm sản phẩm, xoá data cũ từ form và get mới
		app.imgFormData.delete("file[]");
		app.imgFormData.delete("product");
		app.imgFormData.delete("productOption");
		app.imgFormData.delete("producer");
		//lấy từng ảnh trong mảng gán vào formData
		app.arrImg.forEach((value) => {
			app.imgFormData.append("file[]", value);
		});
		let formElement = $("#fAddProduct");
		app.showLoadingButton("#fAddProduct");
		const url = "http://localhost:8080/admin/product/add";
		const producer = {
			"id": `${formElement.querySelector("#productProducer")
				.options[formElement.querySelector("#productProducer").selectedIndex].value}`,
			"name": `${formElement.querySelector("#productProducer")
				.options[formElement.querySelector("#productProducer").selectedIndex].text}`,
		};
		const product = {
			"name": `${formElement.querySelector("#productName").value}`,
			"introdution": `${tinymce.get("productIntrodution").getContent()}`,
		};
		const productOption = {
			"color": `${formElement.querySelector("#productOptionColor").value}`,
			"rom": `${formElement.querySelector("#productOptionRom").value}`,
			"ram": `${formElement.querySelector("#productOptionRam").value}`,
			"monitor": `${formElement.querySelector("#productOptionMonitor").value}`,
			"frontCamera": `${formElement.querySelector("#productOptionFrontCamera").value}`,
			"rearCamera": `${formElement.querySelector("#productOptionRearCamera").value}`,
			"cpu": `${formElement.querySelector("#productOptionCpu").value}`,
			"gpu": `${formElement.querySelector("#productOptionGpu").value}`,
			"pin": `${formElement.querySelector("#productOptionPin").value}`,
			"allSpecifications": `${tinymce.get("productOptionAllSpecifications").getContent()}`,
			"sellingPrice": `${formElement.querySelector("#productOptionSellingPrice").value}`,
			"costPrice": `${formElement.querySelector("#productOptionCostPrice").value}`,
		};
		producerJson = JSON.stringify(producer);
		productJson = JSON.stringify(product);
		productOptionJson = JSON.stringify(productOption);
		let thumbnail = document.getElementById("imgProduct").files[0];
		app.imgFormData.append("producer", producerJson);
		app.imgFormData.append("product", productJson);
		app.imgFormData.append("productOption", productOptionJson);
		app.imgFormData.append("thumbnail", thumbnail);

		//obj.ProductImage = app.imgFormData;
		const response = await fetch(url, {
			method: "POST",
			//header: {
			//"Content-Type": "application/json",
			//},
			body: app.imgFormData,
		});
		const data = await response.json();
		if (data.status == "success") {
			$("#listImgDetails").innerHTML = "";
			app.arrImg.length = 0;
		}
		//Ẩn nút loading
		app.hideLoadingButton("#fAddProduct");
		//hiện flash message
		new window.FlashMessage(data.message, data.status, {
			progress: true,
			interactive: true,
			timeout: 5000,
			appear_delay: 200,
		});

		console.log(data);

	},
	showModalIntrodution: (e) => {
		e.preventDefault();
		let btnViewMore = $("#btnViewMoreIntrodution");
		let introdutionContent =
			e.target.parentElement.querySelector("#Introdution").innerHTML;
		if (btnViewMore && introdutionContent) {
			let modal = `<!-- Modal introdution -->
      <div class="modal-introdution flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]">
          <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[800px] tablet:w-[500px] mobile:w-full overflow-hidden">
              <!-- Header -->
              <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-between items-center">
                  <h3 class="text-white uppercase font-bold">Giới thiệu sản phẩm</h3>
                  <i onclick="app.removeModalIntrodution(event)" class="text-white font-bold text-[16px] hover:cursor-pointer hover:opacity-80 far fa-times-circle"></i>
              </div>
              <!-- Content -->
              <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                  <div class="p-5 w-full">
                          ${introdutionContent}
                  </div>
              </div>
              <!-- Footer -->
              <div class="rounded-br-lg rounded-bl-lg flex bg-[#f5f5f5] p-5 justify-between items-center">
                  <button onclick="app.removeModalIntrodution(event)" class="remove-modal-introdution text-white uppercase bg-blue-500 w-full rounded-xl p-2 font-bold">Đóng</button>
              </div>
          </div>
      </div>`;
			document.body.innerHTML += modal;
		}
	},
	removeModalIntrodution: (e) => {
		let modalElement = e.target.closest(".modal-introdution");
		if (modalElement) {
			modalElement.classList.add("fadeOut");
			setTimeout(() => {
				modalElement.remove();
			}, 300);
		}
	},
	showModalPostContent: (e) => {
		e.preventDefault();
		let btnViewMore = $("#btnViewMorePostContent");
		let postContent =
			e.target.parentElement.querySelector("#postContent").innerHTML;
		if (btnViewMore && postContent) {
			let modal = `<!-- Modal introdution -->
      <div class="modal-post-content flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]">
          <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[800px] tablet:w-[500px] mobile:w-full overflow-hidden">
              <!-- Header -->
              <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-between items-center">
                  <h3 class="text-white uppercase font-bold">Nội dung bài viết</h3>
                  <i onclick="app.removeModalPostContent(event)" class="text-white font-bold text-[16px] hover:cursor-pointer hover:opacity-80 far fa-times-circle"></i>
              </div>
              <!-- Content -->
              <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                  <div class="p-5 w-full">
                          ${postContent}
                  </div>
              </div>
              <!-- Footer -->
              <div class="rounded-br-lg rounded-bl-lg flex bg-[#f5f5f5] p-5 justify-between items-center">
                  <button onclick="app.removeModalPostContent(event)" class="remove-modal-introdution text-white uppercase bg-blue-500 w-full rounded-xl p-2 font-bold">Đóng</button>
              </div>
          </div>
      </div>`;
			document.body.innerHTML += modal;
		}
	},
	removeModalPostContent: (e) => {
		let modalElement = e.target.closest(".modal-post-content");
		if (modalElement) {
			modalElement.classList.add("fadeOut");
			setTimeout(() => {
				modalElement.remove();
			}, 300);
		}
	},
	handleRemoveProducer: () => {
		let nameProduct = $$(".removeProducer");
		if (nameProduct) {
			nameProduct.forEach((element) => {
				element.onclick = () => {
					let id = element.closest("tr").dataset.id;
					let value = element
						.closest("tr")
						.querySelector(".nameProducer")
						.value.trim();
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
                <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
                  <!-- Header -->
                  <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                      <h3 class="text-white uppercase font-bold">Xác nhận</h3>
    
                  </div>
                  <!-- Content -->
                  <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                      <div class="p-5">
                        <p class="font-bold">Bạn chắc chắn muốn xoá ${value}?</p>
                      </div>
                  </div>
                  <!-- Footer -->
                  <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                      <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                      <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
                  </div>
                </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							app.handleDeleteAPI("/admin/producer/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("tr").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("tr").remove();
									}, 300);
								}
							});
							divElement.remove();
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				};
			});
		}
	},
	handleEditProducer: () => {
		let btnEditElement = $$(".editProducer");
		if (btnEditElement) {
			btnEditElement.forEach((element) => {
				element.onclick = () => {
					//Lấy DOM của element trước khi nó biến mất
					let trDOM = element.closest("tr");
					let actionIcons = `<i class="btnSaveProducer p-1 mr-1 hover:cursor-pointer hover:text-green-500 fas fa-check"></i>
          <i class="btnDontSaveProducer p-1 mr-1 hover:cursor-pointer hover:text-slate-500 fas fa-times"></i>
          `;
					let defaultActionIcons = `<i
          class="hover:text-blue-500 hover:cursor-pointer p-1 mr-1 fas fa-pen editProducer"></i>
      <i
          class="hover:text-red-500 hover:cursor-pointer p-1 mr-1 fas fa-trash removeProducer"></i>`;
					//Enable input
					element
						.closest("tr")
						.querySelector(".nameProducer")
						.removeAttribute("disabled");
					element.closest("tr").querySelector(".nameProducer").focus();
					//old value
					oldValue = element
						.closest("tr")
						.querySelector(".nameProducer").value;
					//Đổi các icon của action thành lưu và huỷ
					let ActionIconsElement = element.closest(".actionBtn");
					ActionIconsElement.innerHTML = actionIcons;

					let saveBtn = trDOM.querySelector(".btnSaveProducer");
					let dontSaveBtn = trDOM.querySelector(".btnDontSaveProducer");
					console.log(saveBtn);
					if (saveBtn && dontSaveBtn) {
						saveBtn.onclick = (e) => {
							//Lấy element trước khi thay dổi các icon làm mất
							inputValue = e.target
								.closest("tr")
								.querySelector(".fProducerName");

							//Disable input
							e.target
								.closest("tr")
								.querySelector(".nameProducer")
								.setAttribute("disabled", "disabled");

							//new value
							let newValue = e.target
								.closest("tr")
								.querySelector(".nameProducer").value;
							//lấy dữ liệu
							let data = {};
							let id = e.target.closest("tr").dataset.id;
							console.log(id);
							data.id = id;
							data.name = newValue;
							formdata = new FormData();
							formdata.append("producer", JSON.stringify(data));
							//Call API SaveProducer và .then để đợi các giá trị trả về
							app.handleSaveAPI("/admin/producer/", id, formdata).then((response) => {
								console.log(response);
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {

								} else {
									//Khi status != success
									//Reset lại giá trị của input nếu lưu không thành công
									inputValue
										.reset();

								}
							});



							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove
							app.handleEditProducer();
							app.handleRemoveProducer();
						};

						dontSaveBtn.onclick = (e) => {
							//Reset lại giá trị của input néu không lưu
							e.target
								.closest("tr")
								.querySelector(".fProducerName")
								.reset();
							//Disable input
							e.target
								.closest("tr")
								.querySelector(".nameProducer")
								.setAttribute("disabled", "disabled");
							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove

							app.handleEditProducer();
							app.handleRemoveProducer();
						};
					}
				};
			});
		}
	},
	handleAddProducer: () => {
		let btnAddEleemnt = $(".btnAddProducer");
		if (btnAddEleemnt) {
			btnAddEleemnt.onclick = (e) => {
				let divElement = document.createElement("div");
				divElement.setAttribute(
					"class",
					"modal-addProducer flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
				);
				divElement.innerHTML = `
        <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[800px] tablet:w-[500px] mobile:w-full overflow-hidden">
          <!-- Header -->
          <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-between items-center">
              <h3 class="text-white uppercase font-bold">Thêm nhà sản xuất</h3>
              <i class="btnCancelAddProducer text-white font-bold text-[16px] hover:cursor-pointer hover:opacity-80 far fa-times-circle"></i>
          </div>
          <!-- Content -->
              <form action="" class=" border-t-2 border-solid border-t-white shadow-lg bg-blue-500 p-3 relative">
              <div class="form-group row m-[24px_0px] items-center">
                  <label class="text-white font-bold col l-12 md-12 c-12 tablet:pb-2 mobile:pb-2" for="productName">Tên nhà sản xuất</label>
                  <input name="producerName" placeholder="Tên nhà sản xuât..." class="focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5 col l-12 md-12 c-12" type="text" id="producerName">
                  <span class="error-message col l-o-4 text-red-600"></span>
              </div>
    
    
              <div class="row mx-1 justify-between">
                  <button type="button" class="btnSubmitAddProducer  col l-6 btn-secondary">Lưu</button>
                  <button type="button" class="btnCancelAddProducer col l-4 btn-cancel text-centerbtnCancel">Huỷ</button>
              </div>
          </form>
        </div>
        `;

				let modal = `
            <div class="modal-addProducer flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]">
                
            </div>`;
				document.body.appendChild(divElement);

				let btnAddElement = $(".btnSubmitAddProducer");
				let btnCancelElement = $$(".btnCancelAddProducer");
				let data = {};
				if (btnAddElement) {
					btnAddElement.onclick = (e) => {
						let producerName = btnAddElement
							.closest("form")
							.querySelector("input[name='producerName']").value;


						//dữ liệu phải khác rỗng
						if (producerName != "") {
							app.removeModalAddProducer(e);
							data.name = producerName;
							formdata = new FormData();
							formdata.append("producer", JSON.stringify(data));
							app.handleCreateAPI("/admin/producer/", formdata).then((response) => {
								console.log(response);
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									let body = $(".bodyListProducer");
									if (body) {
										let tr = document.createElement("tr");
										tr.setAttribute("class", "even:bg-blue-100 odd:bg-white");
										tr.setAttribute("data-id", response.data.id);
										tr.innerHTML = `
							<td class="p-2 align-middle">
                                        <i
                                            class="text-yellow-500 fas fa-plus-circle hover:cursor-pointer hover:opacity-80"></i>
                                    </td>
                                    <td class="p-2 align-middle">
                                        1
                                    </td>

                                    <td class="p-2 align-middle ">
                                        <form class="fProducerName">
                                        	<input
                                            class="bg-transparent focus-visible:outline-none nameProducer text-center"
                                            type="text" value=${producerName} disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle">
                                        <div class="flex items-center justify-center actionBtn">

                                            <i 
                                                class="hover:text-blue-500 hover:cursor-pointer p-1 mr-1 fas fa-pen editProducer"></i>
                                            <i 
                                                class="hover:text-red-500 hover:cursor-pointer p-1 mr-1 fas fa-trash removeProducer"></i>
                                        </div>
                                    </td>
							`;
										body.appendChild(tr);
										app.handleEditProducer();
										app.handleRemoveProducer();
									}
								} else {
									//Khi status != success
								}
							});

						}
					};
				}
				if (btnCancelElement) {
					btnCancelElement.forEach((element) => {
						element.onclick = (e) => {
							app.removeModalAddProducer(e);
						};
					});
				}
			};
		}
	},
	removeModalAddProducer: (e) => {
		let modalElement = e.target.closest(".modal-addProducer");
		modalElement.classList.add("fadeOut");
		setTimeout(() => {
			modalElement.remove();
		}, 300);
	},
	handleRemoveSupplier: () => {
		let nameProduct = $$(".removeSupplier");
		if (nameProduct) {
			nameProduct.forEach((element) => {
				element.onclick = () => {
					let id = element.closest("tr").dataset.id;
					let value = element
						.closest("tr")
						.querySelector(".nameSupplier")
						.value.trim();
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
                <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
                  <!-- Header -->
                  <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                      <h3 class="text-white uppercase font-bold">Xác nhận</h3>
    
                  </div>
                  <!-- Content -->
                  <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                      <div class="p-5">
                        <p class="font-bold">Bạn chắc chắn muốn xoá ${value}?</p>
                      </div>
                  </div>
                  <!-- Footer -->
                  <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                      <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                      <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
                  </div>
                </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							app.handleDeleteAPI("/admin/supplier/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("tr").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("tr").remove();
									}, 300);
								}
							});
							divElement.remove();
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				};
			});
		}
	},
	handleEditSupplier: () => {
		let btnEditElement = $$(".editSupplier");
		if (btnEditElement) {
			btnEditElement.forEach((element) => {
				element.onclick = () => {
					//Lấy DOM của element trước khi nó biến mất
					let trDOM = element.closest("tr");
					let actionIcons = `<i class="btnSaveSupplier p-1 mr-1 hover:cursor-pointer hover:text-green-500 fas fa-check"></i>
          <i class="btnDontSaveSupplier p-1 mr-1 hover:cursor-pointer hover:text-slate-500 fas fa-times"></i>
          `;
					let defaultActionIcons = `<i
          class="hover:text-blue-500 hover:cursor-pointer p-1 mr-1 fas fa-pen editSupplier"></i>
      <i
          class="hover:text-red-500 hover:cursor-pointer p-1 mr-1 fas fa-trash removeSupplier"></i>`;
					//Enable input

					//name
					element
						.closest("tr")
						.querySelector(".nameSupplier")
						.removeAttribute("disabled");
					element.closest("tr").querySelector(".nameSupplier").focus();

					//address
					element
						.closest("tr")
						.querySelector(".addressSupplier")
						.removeAttribute("disabled");

					//numberphone
					element
						.closest("tr")
						.querySelector(".numberphoneSupplier")
						.removeAttribute("disabled");

					//Đổi các icon của action thành lưu và huỷ
					let ActionIconsElement = element.closest(".actionBtn");
					ActionIconsElement.innerHTML = actionIcons;

					let saveBtn = trDOM.querySelector(".btnSaveSupplier");
					let dontSaveBtn = trDOM.querySelector(".btnDontSaveSupplier");
					console.log(saveBtn);
					if (saveBtn && dontSaveBtn) {
						saveBtn.onclick = (e) => {
							//Lấy element trước khi thay dổi các icon làm mất
							inputNameValue = e.target
								.closest("tr")
								.querySelector(".fSupplierName");

							inputAddressValue = e.target
								.closest("tr")
								.querySelector(".fSupplierAddress");

							inputNumberphoneValue = e.target
								.closest("tr")
								.querySelector(".fSupplierPhone");

							//Disable input
							e.target
								.closest("tr")
								.querySelector(".nameSupplier")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".addressSupplier")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".numberphoneSupplier")
								.setAttribute("disabled", "disabled");

							//new value
							let newNameValue = e.target
								.closest("tr")
								.querySelector(".nameSupplier").value;
							let newAddressValue = e.target
								.closest("tr")
								.querySelector(".addressSupplier").value;
							let newNumberphoneValue = e.target
								.closest("tr")
								.querySelector(".numberphoneSupplier").value;
							//lấy dữ liệu
							let data = {};
							let id = e.target.closest("tr").dataset.id;
							console.log(id);
							data.id = id;
							data.name = newNameValue;
							data.address = newAddressValue;
							data.numberphone = newNumberphoneValue;
							formdata = new FormData();
							formdata.append("supplier", JSON.stringify(data));
							//Call API SaveProducer và .then để đợi các giá trị trả về
							app.handleSaveAPI("/admin/supplier/", id, formdata).then((response) => {
								console.log(response);
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {

								} else {
									//Khi status != success
									//Reset lại giá trị của input ở trong form(inputNameValue) nếu lưu không thành công
									//mỗi trường input sẽ được wrap lại bằng 1 form
									inputNameValue
										.reset();
									inputAddressValue
										.reset();
									inputNumberphoneValue
										.reset();

								}
							});



							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove
							app.handleEditSupplier();
							app.handleRemoveSupplier();
						};

						dontSaveBtn.onclick = (e) => {
							//Reset lại giá trị của input néu không lưu
							e.target
								.closest("tr")
								.querySelector(".fSupplierName")
								.reset();

							e.target
								.closest("tr")
								.querySelector(".fSupplierAddress")
								.reset();

							e.target
								.closest("tr")
								.querySelector(".fSupplierPhone")
								.reset();

							//Disable input
							e.target
								.closest("tr")
								.querySelector(".nameSupplier")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".addressSupplier")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".numberphoneSupplier")
								.setAttribute("disabled", "disabled");
							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove

							app.handleEditSupplier();
							app.handleRemoveSupplier();
						};
					}
				};
			});
		}
	},
	handleAddSupplier: () => {
		let btnAddEleemnt = $(".btnAddSupplier");
		if (btnAddEleemnt) {
			btnAddEleemnt.onclick = (e) => {
				let divElement = document.createElement("div");
				divElement.setAttribute(
					"class",
					"modal-addSupplier flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
				);
				divElement.innerHTML = `
        <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[800px] tablet:w-[500px] mobile:w-full overflow-hidden">
          <!-- Header -->
          <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-between items-center">
              <h3 class="text-white uppercase font-bold">Thêm nhà sản xuất</h3>
              <i class="btnCancelAddSupplier text-white font-bold text-[16px] hover:cursor-pointer hover:opacity-80 far fa-times-circle"></i>
          </div>
          <!-- Content -->
              <form action="" class=" border-t-2 border-solid border-t-white shadow-lg bg-blue-500 p-3 relative">
              <div class="form-group row m-[24px_0px] items-center">
                  <label class="text-white font-bold col l-12 md-12 c-12 tablet:pb-2 mobile:pb-2" for="supplierName">Tên nhà sản xuất</label>
                  <input name="supplierName" placeholder="Tên nhà cung cấp..." class="focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5 col l-12 md-12 c-12" type="text" id="supplierName">
                  <span class="error-message col l-o-4 text-red-600"></span>
              </div>
              <div class="form-group row m-[24px_0px] items-center">
                  <label class="text-white font-bold col l-12 md-12 c-12 tablet:pb-2 mobile:pb-2" for="supplierAddress">Địa chỉ</label>
                  <input name="supplierAddress" placeholder="Địa chỉ nhà cung cấp..." class="focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5 col l-12 md-12 c-12" type="text" id="supplierAddress">
                  <span class="error-message col l-o-4 text-red-600"></span>
              </div>
              <div class="form-group row m-[24px_0px] items-center">
                  <label class="text-white font-bold col l-12 md-12 c-12 tablet:pb-2 mobile:pb-2" for="supplierPhone">Số điện thoại</label>
                  <input name="supplierPhone" placeholder="Số điện thoại..." class="focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5 col l-12 md-12 c-12" type="text" id="supplierPhone">
                  <span class="error-message col l-o-4 text-red-600"></span>
              </div>
    
    
              <div class="row mx-1 justify-between">
                  <button type="button" class="btnSubmitAddSupplier  col l-6 btn-secondary">Lưu</button>
                  <button type="button" class="btnCancelAddSupplier col l-4 btn-cancel text-centerbtnCancel">Huỷ</button>
              </div>
          </form>
        </div>
        `;

				document.body.appendChild(divElement);

				let btnAddElement = $(".btnSubmitAddSupplier");
				let btnCancelElement = $$(".btnCancelAddSupplier");
				let data = {};
				if (btnAddElement) {
					btnAddElement.onclick = (e) => {
						let supplierName = btnAddElement
							.closest("form")
							.querySelector("input[name='supplierName']").value;
						let supplierAddress = btnAddElement
							.closest("form")
							.querySelector("input[name='supplierAddress']").value;
						let supplierPhone = btnAddElement
							.closest("form")
							.querySelector("input[name='supplierPhone']").value;

						//dữ liệu phải khác rỗng
						if (supplierName != "" && supplierAddress != "" && supplierPhone != "") {
							app.removeModalAddSupplier(e);
							data.name = supplierName;
							data.address = supplierAddress;
							data.numberphone = supplierPhone;
							formdata = new FormData();
							formdata.append("supplier", JSON.stringify(data));
							app.handleCreateAPI("/admin/supplier/", formdata).then((response) => {
								console.log(response);
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									let body = $(".bodyListSupplier");
									if (body) {
										let tr = document.createElement("tr");
										tr.setAttribute("class", "even:bg-blue-100 odd:bg-white");
										tr.setAttribute("data-id", response.data.id);
										tr.innerHTML = `
							<td class="p-2 align-middle">
                                        <i
                                            class="text-yellow-500 fas fa-plus-circle hover:cursor-pointer hover:opacity-80"></i>
                                    </td>
                                    <td class="p-2 align-middle">
                                        1
                                    </td>

                                    <td class="p-2 align-middle ">
                                        <form class="fSupplierName">
                                        	<input
                                            class="bg-transparent focus-visible:outline-none nameSupplier text-center"
                                            type="text" value=${supplierName} disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle ">
                                        <form class="fSupplierAddress">
                                        	<input
                                            class="bg-transparent focus-visible:outline-none addressSupplier text-center"
                                            type="text" value=${supplierAddress} disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle ">
                                        <form class="fSupplierPhone">
                                        	<input
                                            class="bg-transparent focus-visible:outline-none numberphoneSupplier text-center"
                                            type="text" value=${supplierPhone} disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle">
                                        <div class="flex items-center justify-center actionBtn">

                                            <i 
                                                class="hover:text-blue-500 hover:cursor-pointer p-1 mr-1 fas fa-pen editSupplier"></i>
                                            <i 
                                                class="hover:text-red-500 hover:cursor-pointer p-1 mr-1 fas fa-trash removeSupplier"></i>
                                        </div>
                                    </td>
							`;
										body.appendChild(tr);
										app.handleEditSupplier();
										app.handleRemoveSupplier();
									}
								} else {
									//Khi status != success
								}
							});

						}
					};
				}
				if (btnCancelElement) {
					btnCancelElement.forEach((element) => {
						element.onclick = (e) => {
							app.removeModalAddSupplier(e);
						};
					});
				}
			};
		}
	},
	removeModalAddSupplier: (e) => {
		let modalElement = e.target.closest(".modal-addSupplier");
		modalElement.classList.add("fadeOut");
		setTimeout(() => {
			modalElement.remove();
		}, 300);
	},
	handleRemoveReceipt: () => {
		let nameProduct = $$(".removeReceipt");
		if (nameProduct) {
			nameProduct.forEach((element) => {
				element.onclick = () => {
					let id = element.closest("tr").dataset.id;
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
                <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
                  <!-- Header -->
                  <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                      <h3 class="text-white uppercase font-bold">Xác nhận</h3>
    
                  </div>
                  <!-- Content -->
                  <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                      <div class="p-5">
                        <p class="font-bold">Bạn chắc chắn muốn xoá?</p>
                      </div>
                  </div>
                  <!-- Footer -->
                  <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                      <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                      <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
                  </div>
                </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							app.handleDeleteAPI("/admin/receipt/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("tr").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("tr").remove();
									}, 300);
								}
							});
							divElement.remove();
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				};
			});
		}
	},
	handleEditReceipt: () => {
		let btnEditElement = $$(".editReceipt");
		if (btnEditElement) {
			btnEditElement.forEach((element) => {
				element.onclick = () => {
					let actionIcons = `<i class="btnSaveReceipt p-1 mr-1 hover:cursor-pointer hover:text-green-500 fas fa-check"></i>
          <i class="btnDontSaveReceipt p-1 mr-1 hover:cursor-pointer hover:text-slate-500 fas fa-times"></i>
          `;
					let defaultActionIcons = `<i
          class="hover:text-blue-500 hover:cursor-pointer p-1 mr-1 fas fa-pen editSupplier"></i>
      <i
          class="hover:text-red-500 hover:cursor-pointer p-1 mr-1 fas fa-trash removeSupplier"></i>`;
					//Enable input

					//store
					element
						.closest("tr")
						.querySelector(".storeReceipt")
						.removeAttribute("disabled");
					element.closest("tr").querySelector(".storeReceipt").focus();

					//supplier
					element
						.closest("tr")
						.querySelector(".supplierReceipt")
						.removeAttribute("disabled");

					//optionProduct
					element
						.closest("tr")
						.querySelector(".optionProductReceipt")
						.removeAttribute("disabled");

					//Đổi các icon của action thành lưu và huỷ
					let ActionIconsElement = element.closest(".actionBtn");
					ActionIconsElement.innerHTML = actionIcons;

					let saveBtn = $(".btnSaveReceipt");
					let dontSaveBtn = $(".btnDontSaveReceipt");
					console.log(saveBtn);
					if (saveBtn && dontSaveBtn) {
						saveBtn.onclick = (e) => {
							//Lấy element trước khi thay dổi các icon làm mất
							store = e.target
								.closest("tr")
								.querySelector(".fStore");

							supplier = e.target
								.closest("tr")
								.querySelector(".fSupplier");

							optionProduct = e.target
								.closest("tr")
								.querySelector(".fProductOption");
							receiptQuantity = e.target
								.closest("tr")
								.querySelector(".fReceiptQuantity");
							//Disable input
							e.target
								.closest("tr")
								.querySelector(".storeReceipt")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".supplierReceipt")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".productOptionReceipt")
								.setAttribute("disabled", "disabled");

							//new value
							let storeId = e.target
								.closest("tr")
								.querySelector(".storeReceipt").value;
							let supplierId = e.target
								.closest("tr")
								.querySelector(".supplierReceipt").value;
							let productOptionId = e.target
								.closest("tr")
								.querySelector(".productOptionReceipt").value;
							let quantity = e.target
								.closest("tr")
								.querySelector(".receiptQuantity").value;
							//lấy dữ liệu
							let data = {};
							let id = e.target.closest("tr").dataset.id;
							console.log(id);
							data.receiptId = id;
							data.storeId = storeId;
							data.supplierId = supplierId;
							data.productOptionId = productOptionId;
							data.quantity = quantity;

							formdata = new FormData();
							formdata
							//Call API SaveProducer và .then để đợi các giá trị trả về
							app.handleSaveAPI("/admin/receipt/", id, formdata).then((response) => {
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {

								} else {
									//Khi status != success
									//Reset lại giá trị của input ở trong form(inputNameValue) nếu lưu không thành công
									//mỗi trường input sẽ được wrap lại bằng 1 form
									store
										.reset();
									supplier
										.reset();
									optionProduct
										.reset();
									receiptQuantity
										.reset();
								}
							});



							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove
							app.handleEditReceipt();
							app.handleRemoveReceipt();
						};

						dontSaveBtn.onclick = (e) => {
							//Reset lại giá trị của input néu không lưu
							e.target
								.closest("tr")
								.querySelector(".fStore")
								.reset();

							e.target
								.closest("tr")
								.querySelector(".fSupplier")
								.reset();

							e.target
								.closest("tr")
								.querySelector(".fProductOption")
								.reset();

							e.target
								.closest("tr")
								.querySelector(".fReceiptQuantity")
								.reset();

							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove

							app.handleEditSupplier();
							app.handleRemoveSupplier();
						};
					}
				};
			});
		}
	},
	handleAddReceipt: () => {
		//2 chu e
		let btnAddEleemnt = $(".btnAddReceipt");
		if (btnAddEleemnt) {

			btnAddEleemnt.onclick = (e) => {
				let modal = $(".modal-addReceipt");
				if (modal) {
					modal.classList.remove("fadeOut");
					modal.classList.toggle("hidden");
					modal.classList.toggle("flex");
					modal.classList.toggle("fixed");
				}
				//mot chu e
				let btnAddElement = $(".btnSubmitAddReceipt");
				let btnCancelElement = $$(".btnCancelAddReceipt");
				if (btnAddElement) {
					btnAddElement.onclick = (e) => {
						//lấy dữ liệu từ select và input
						//Chuyển đổi về kiểu int
						let storeId = parseInt(e.target
							.closest("#fAddReceipt")
							.querySelector(".store").value);

						let supplierId = parseInt(e.target
							.closest("#fAddReceipt")
							.querySelector(".supplier").value);

						let items = e.target.closest("#fAddReceipt").querySelectorAll(".productOptionItem");
						formdata = new FormData();
						formdata.append("items", "null");
						//Items này thuộc bảng đã được click add, vì ở trên đã có e.target
						if (items) {
							items.forEach((element) => {
								let data = {};
								let productOptionId = parseInt(element.querySelector(".productOption").value);
								let quantity = parseInt(element.querySelector(".productOptionQuantity").value);
								let price = parseInt(element.querySelector(".receiptDetailPrice").value);
								data.productOptionId = productOptionId;
								data.quantity = quantity;
								data.price = price;
								formdata.append("items", JSON.stringify(data));

							})
						}
						//dữ liệu phải khác rỗng và list item phải hơn 0 
						if (storeId != null && supplierId != null && items.length > 0) {
							app.removeModalAddReceipt(e);

							formdata.append("storeId", JSON.stringify(storeId));
							formdata.append("supplierId", JSON.stringify(supplierId));
							console.log(formdata.getAll("items"));
							app.handleCreateAPI("/admin/receipt/", formdata).then((response) => {
								console.log(response);
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									//location.reload();
								} else {
									//Khi status != success
								}
							});

						}
					};
				}
				if (btnCancelElement) {
					btnCancelElement.forEach((element) => {
						element.onclick = (e) => {
							app.removeModalAddReceipt(e);
						};
					});
				}
			};
		}
	},
	removeModalAddReceipt: (e) => {
		let modalElement = e.target.closest(".modal-addReceipt");
		modalElement.classList.add("fadeOut");
		setTimeout(() => {
			modalElement.classList.toggle("hidden");
			modalElement.classList.toggle("fixed");
			modalElement.classList.toggle("flex");
		}, 300);
	},
	htmlProductOptionInReceipt: "",
	handleAddMoreProductOptionInModal: () => {
		let btnElement = $(".btnAddMoreProductOption");
		if (btnElement) {
			btnElement.onclick = () => {
				let productOptionList = $(".productOptionList");
				let productOptionItem = document.createElement("div");
				if (productOptionList) {
					$(".productOptionItem") ? app.htmlProductOptionInReceipt = $(".productOptionItem").innerHTML : "";
					productOptionItem.setAttribute("class", "productOptionItem row");
					productOptionItem.innerHTML = app.htmlProductOptionInReceipt;
					productOptionList.appendChild(productOptionItem);
				}
				app.handleRemoveProductOptionInModal();
			}
		}
	},
	handleRemoveProductOptionInModal: () => {
		let btnElements = $$(".btnRemoveProductOptionInReceipt");
		if (btnElements) {
			btnElements.forEach((element) => {
				element.onclick = (e) => {
					e.target.closest(".productOptionItem").classList.add("fadeOut");
					setTimeout(() => {
						e.target.closest(".productOptionItem").remove();
					}, 300)
				}
			})
		}
	},
	handleAddProductOption: () => {
		let btnAddElement = $(".btnAddProductOption");
		if (btnAddElement) {
			btnAddElement.onclick = async () => {
				//Mỗi khi click thêm sản phẩm, xoá data cũ từ form và get mới
				app.imgFormData.delete("file[]");
				app.imgFormData.delete("product");
				app.imgFormData.delete("productOption");
				app.imgFormData.delete("producer");
				//lấy từng ảnh trong mảng gán vào formData
				app.arrImg.forEach((value) => {
					app.imgFormData.append("file[]", value);
				});
				let formElement = $("#fAddProductOption");
				app.showLoadingButton("#fAddProductOption");
				const url = "http://localhost:8080/admin/productOption/add";
				const productOption = {
					"color": `${formElement.querySelector("#productOptionColor").value}`,
					"rom": `${formElement.querySelector("#productOptionRom").value}`,
					"ram": `${formElement.querySelector("#productOptionRam").value}`,
					"monitor": `${formElement.querySelector("#productOptionMonitor").value}`,
					"frontCamera": `${formElement.querySelector("#productOptionFrontCamera").value}`,
					"rearCamera": `${formElement.querySelector("#productOptionRearCamera").value}`,
					"cpu": `${formElement.querySelector("#productOptionCpu").value}`,
					"gpu": `${formElement.querySelector("#productOptionGpu").value}`,
					"pin": `${formElement.querySelector("#productOptionPin").value}`,
					"allSpecifications": `${tinymce.get("productOptionAllSpecifications").getContent()}`,
					"sellingPrice": `${formElement.querySelector("#productOptionSellingPrice").value}`,
					"costPrice": `${formElement.querySelector("#productOptionCostPrice").value}`,
				};
				let productId = parseInt($("#productId").value);
				productIdJson = JSON.stringify(productId);
				productOptionJson = JSON.stringify(productOption);
				app.imgFormData.append("productId", productIdJson);
				app.imgFormData.append("productOption", productOptionJson);

				//obj.ProductImage = app.imgFormData;
				const response = await fetch(url, {
					method: "POST",
					//header: {
					//"Content-Type": "application/json",
					//},
					body: app.imgFormData,
				});
				const data = await response.json();
				if (data.status == "success") {
					$("#listImgDetails").innerHTML = "";
					app.arrImg.length = 0;
				}
				//Ẩn nút loading
				app.hideLoadingButton("#fAddProductOption");
				//hiện flash message
				new window.FlashMessage(data.message, data.status, {
					progress: true,
					interactive: true,
					timeout: 5000,
					appear_delay: 200,
				});

				console.log(data);
			}


		}
	},
	handleEditProductOption: () => {
		let btnSave = $$(".btnUpdateProductOption");
		let btnCancel = $$(".btnCancelProductOption");
		let btnAddMoreImage = $$(".btnAddDetailsImg");
		let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i; //các tập tin cho phép
		if (btnAddMoreImage && btnSave && btnCancel) {
			btnAddMoreImage.forEach((element) => {
				element.onclick = (e) => {
					//bắt sự kiện onclick thêm ảnh mới
					let formElement = e.target.closest(".fEditProductOption");
					let table = formElement.querySelector(".listImgDetails");
					let productOptionId = formElement.querySelector(".productOptionId").value;
					//đây là cú pháp khai báo biến toàn cầu
					if (!window['arrImg' + productOptionId]) {
						window['arrImg' + productOptionId] = [];
					}
					let arrImg = window['arrImg' + productOptionId];
					console.log(arrImg);
					//click mở input để chọn file
					formElement.querySelector(".detailImgsProduct").click();
					let input = formElement.querySelector(".detailImgsProduct");
					//bắt sự kiện up file mới
					input.onchange = () => {
						//hiển thị lỗi
						let errorElement = formElement.querySelector(".sumaryErrorsUploadFiles");
						if (errorElement) {
							errorElement.textContent = "";
						}
						for (let file of input.files) {
							//kiểm tra đuôi file
							if (!allowedExtensions.exec(file.name)) {
								errorElement.textContent = "Lỗi không đúng định dạng ảnh!";
								return;
							}
							//kiểm tra ảnh đã tồn tại trong mảng file để gửi lên server chưa
							for (storedFile of arrImg) {
								if (storedFile.name == file.name) {
									errorElement.textContent = "Ảnh đã tồn tại!";
									return;
								}
							}
							//đưa file vào mảng toàn cầu
							arrImg.push(file);
							//DOM thêm dòng mới vào table
							let tr = document.createElement("tr");
							tr.setAttribute("class", "border-b-[1px] border-solid border-[#aaa]");
							tr.setAttribute("data-name", file.name);
							let url = URL.createObjectURL(file);
							tr.innerHTML = `
									<td class="align-middle">
							              <img class="w-[80px] h-[80px] object-contain p-2" src="${url}" alt="">
							          </td>
							          <td class="align-middle">
							              <div class="p-1">
							                  <span>${file.name}</span>
							                  
							              </div>
							          </td>
							          <td class="align-middle text-right">
							              <div class=" p-1">
							                  <i title="Xoá" class="fas fa-trash btnRemoveImgsProduct hover:cursor-pointer hover:text-red-500"></i>
							              
							              </div>
							          </td>
							`;
							table.appendChild(tr);
							//bắt sự kiện xoá ảnh
							removeImgTr();
						}


					}
				}
				//khai báo hàm trong hàm dể xử dùng nội bộ ở nhiều vị trí
				//hàm xoá tr (chứa ảnh)
				//và đưa id của ảnh cần xoá vào mảng toàn cầu 
				const removeImgTr = () => {
					let btnRemoveElements = element.closest(".fEditProductOption").querySelectorAll(".btnRemoveImgsProduct");
					if (btnRemoveElements) {
						btnRemoveElements.forEach((element) => {
							//lấy id
							let productOptionId = element.closest(".fEditProductOption").querySelector(".productOptionId").value;
							//bắt sự kiện nhấn vào nút xoá ảnh
							element.onclick = (e) => {
								let tr = e.target.closest("tr");
								let imgId = tr.dataset.id;
								let imgName = tr.dataset.name;
								//trường hợp nếu xoá đã upload(trên database)
								if (imgId) {
									if (!window['imageDeletedIds' + productOptionId]) {
										window['imageDeletedIds' + productOptionId] = [];
									}
									let imageDeletedIds = window['imageDeletedIds' + productOptionId];
									imageDeletedIds.push(imgId);
									console.log(imageDeletedIds);
									tr.classList.add("fadeOut");
									setTimeout(() => {
										tr.remove();
									}, 300)
								} else {
									//trường hợp nếu xoá ảnh chưa up load(trong mảng file)
									if (imgName) {
										let arrImg = window['arrImg' + productOptionId];
										arrImg.forEach((value, index) => {
											//xài vòng lặp forEach để lấy ra file(value) sau đó so sánh name của file với name của data-name
											//nếu tồn tại thì xoá phần tử file trong mảng ở vị trí index
											if (value.name = imgName) {
												//gọi hàm xoá phần tử trong mảng
												arrImg.splice(index, 1);
												console.log("Da xoa hinh anh o trong list File");
											}
										})
										tr.classList.add("fadeOut");
										setTimeout(() => {
											tr.remove();
										}, 300)

									}
								}



							}
						})
					}
				}
				removeImgTr();
			})
		}
		//
		if (btnSave) {
			btnSave.forEach((element) => {
				element.onclick = (e) => {
					//khởi tạo formData
					formData = new FormData();
					//lấy form đang edit
					formElement = e.target.closest(".fEditProductOption");
					//lây các tham số cần thiết
					let productOptionId = formElement.querySelector(".productOptionId").value;
					let arrImg = window['arrImg' + productOptionId];
					let imageDeletedIds = window['imageDeletedIds' + productOptionId];
					const productOption = {
						"color": `${formElement.querySelector(".productOptionColor").value}`,
						"rom": `${formElement.querySelector(".productOptionRom").value}`,
						"ram": `${formElement.querySelector(".productOptionRam").value}`,
						"monitor": `${formElement.querySelector(".productOptionMonitor").value}`,
						"frontCamera": `${formElement.querySelector(".productOptionFrontCamera").value}`,
						"rearCamera": `${formElement.querySelector(".productOptionRearCamera").value}`,
						"cpu": `${formElement.querySelector(".productOptionCpu").value}`,
						"gpu": `${formElement.querySelector(".productOptionGpu").value}`,
						"pin": `${formElement.querySelector(".productOptionPin").value}`,
						"allSpecifications": `${tinymce.get(productOptionId).getContent()}`,
						"sellingPrice": `${formElement.querySelector(".productOptionSellingPrice").value}`,
						"costPrice": `${formElement.querySelector(".productOptionCostPrice").value}`,
					};
					formData.append("productOption", JSON.stringify(productOption));
					console.log(arrImg);
					//Nếu arrImg đã được khởi tạo mới thêm vào formData
					if (arrImg) {
						for (file of arrImg) {
							formData.append("file[]", file);
						}
					}
					//Nếu imageDeletedIds đã được khởi tạo mới thêm vào formData
					if (imageDeletedIds) {
						formData.append("imageDeletedId", imageDeletedIds);
					}

					app.handleSaveAPI("/admin/productOption/", productOptionId, formData).then((response) => {
						console.log(response);
						new window.FlashMessage(response.message, response.status, {
							progress: true,
							interactive: true,
							timeout: 5000,
							appear_delay: 200,
						});
						if (response.status == "success") {

						} else {

						}
					});

				}
			})
		}

		if (btnCancel) {
			btnCancel.forEach((element) => {
				element.onclick = (e) => {
					let formElement = e.target.closest(".fEditProductOption");
					formElement.reset();
					let productOptionId = formElement.querySelector(".productOptionId").value
					let iconElement = $("#iconShowHide" + productOptionId);
					let minusClass = `iconShowHide fas fa-minus-circle`;
					let plusClass = `iconShowHide fas fa-plus-circle`;
					if (formElement) {
						// Form đang hiện
						if (formElement.classList.contains("upHeight")) {
							formElement.classList.remove("block");
							formElement.classList.remove("upHeight");
							// Đổi icon - thành +
							iconElement.setAttribute("class", plusClass);
							setTimeout(() => {
								formElement.classList.add("hidden");
							}, 200);
							formElement.classList.add("reduceHeight");
							// Form đã ẩn
						} else {
							formElement.classList.remove("reduceHeight");
							formElement.classList.remove("hidden");
							formElement.classList.add("block");
							formElement.classList.add("upHeight");
							// Đổi icon + thành -
							iconElement.setAttribute("class", minusClass);
						}
					}

				}
			})
		}
	},
	handleRemoveProductOption: () => {
		let btnRemoves = $$(".btnRemoveProductOption");
		if (btnRemoves) {
			btnRemoves.forEach((element) => {
				element.onclick = (e) => {
					let nameProductOption =
						element.parentElement.querySelector("h3").textContent;
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
            <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
              <!-- Header -->
              <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                  <h3 class="text-white uppercase font-bold">Xác nhận</h3>

              </div>
              <!-- Content -->
              <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                  <div class="p-5">
                    <p class="font-bold">Bạn chắc chắn muốn xoá ${nameProductOption}?</p>
                  </div>
              </div>
              <!-- Footer -->
              <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                  <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                  <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
              </div>
            </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							let id = parseInt(e.target.dataset.id);
							app.handleDeleteAPI("/admin/productOption/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("section").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("section").remove();
									}, 300);

								} else {
									//khi response.status != success

								}
								divElement.remove();
							});
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				}
			})
		}
	},
	handleRemovePromotion: () => {
		let nameProduct = $$(".removePromotion");
		if (nameProduct) {
			nameProduct.forEach((element) => {
				element.onclick = () => {
					let id = element.closest("tr").dataset.id;
					let value = element
						.closest("tr")
						.querySelector(".namePromotion")
						.value.trim();
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
                <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
                  <!-- Header -->
                  <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                      <h3 class="text-white uppercase font-bold">Xác nhận</h3>
    
                  </div>
                  <!-- Content -->
                  <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                      <div class="p-5">
                        <p class="font-bold">Bạn chắc chắn muốn xoá ${value}?</p>
                      </div>
                  </div>
                  <!-- Footer -->
                  <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                      <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                      <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
                  </div>
                </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							app.handleDeleteAPI("/admin/promotion/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("tr").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("tr").remove();
									}, 300);
								}
							});
							divElement.remove();
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				};
			});
		}
	},
	handleEditPromotion: () => {
		let btnEditElement = $$(".editPromotion");
		if (btnEditElement) {
			btnEditElement.forEach((element) => {
				element.onclick = () => {
					//Lấy DOM của element trước khi nó biến mất
					let trDOM = element.closest("tr");

					let actionIcons = `<i class="btnSavePromotion p-1 mr-1 hover:cursor-pointer hover:text-green-500 fas fa-check"></i>
          <i class="btnDontSavePromotion p-1 mr-1 hover:cursor-pointer hover:text-slate-500 fas fa-times"></i>
          `;
					let defaultActionIcons = `<i
          class="hover:text-blue-500 hover:cursor-pointer p-1 mr-1 fas fa-pen editPromotion"></i>
      <i
          class="hover:text-red-500 hover:cursor-pointer p-1 mr-1 fas fa-trash removePromotion"></i>`;
					//Enable input
					element
						.closest("tr")
						.querySelector(".namePromotion")
						.removeAttribute("disabled");
					element.closest("tr").querySelector(".namePromotion").focus();
					element
						.closest("tr")
						.querySelector(".contentPromotion")
						.removeAttribute("disabled");
					element
						.closest("tr")
						.querySelector(".startDatePromotion")
						.removeAttribute("disabled");
					element
						.closest("tr")
						.querySelector(".endDatePromotion")
						.removeAttribute("disabled");
					//Đổi các icon của action thành lưu và huỷ
					let ActionIconsElement = element.closest(".actionBtn");
					ActionIconsElement.innerHTML = actionIcons;
					let saveBtn = trDOM.querySelector(".btnSavePromotion");
					let dontSaveBtn = trDOM.querySelector(".btnDontSavePromotion");
					console.log(saveBtn);
					if (saveBtn && dontSaveBtn) {
						saveBtn.onclick = (e) => {
							//Lấy element trước khi thay dổi các icon làm mất
							fName = e.target
								.closest("tr")
								.querySelector(".fPromotionName");
							fContent = e.target
								.closest("tr")
								.querySelector(".fPromotionContent");
							fStartDate = e.target
								.closest("tr")
								.querySelector(".fPromotionStartDate");
							fEndDate = e.target
								.closest("tr")
								.querySelector(".fPromotionEndDate");
							//Disable input
							e.target
								.closest("tr")
								.querySelector(".namePromotion")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".contentPromotion")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".startDatePromotion")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".endDatePromotion")
								.setAttribute("disabled", "disabled");

							//new value
							let name = e.target
								.closest("tr")
								.querySelector(".namePromotion").value;

							let content = e.target
								.closest("tr")
								.querySelector(".contentPromotion").value;
							let startDate = e.target
								.closest("tr")
								.querySelector(".startDatePromotion").value;
							let endDate = e.target
								.closest("tr")
								.querySelector(".endDatePromotion").value;
							//lấy dữ liệu
							let data = {};
							let id = e.target.closest("tr").dataset.id;
							console.log(id);
							data.name = name;
							data.content = content;
							data.startDate = startDate;
							data.endDate = endDate;
							formdata = new FormData();
							formdata.append("promotion", JSON.stringify(data));
							//Call API SaveProducer và .then để đợi các giá trị trả về
							app.handleSaveAPI("/admin/promotion/", id, formdata).then((response) => {
								console.log(response);
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {

								} else {
									//Khi status != success
									//Reset lại giá trị của input nếu lưu không thành công
									fName
										.reset();
									fContent
										.reset();
									fStartDate
										.reset();
									fEndDate
										.reset();


								}
							});



							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove
							app.handleEditPromotion();
							app.handleRemovePromotion();
						};

						dontSaveBtn.onclick = (e) => {
							//Reset lại giá trị của input néu không lưu
							e.target
								.closest("tr")
								.querySelector(".fPromotionName")
								.reset();
							e.target
								.closest("tr")
								.querySelector(".fPromotionContent")
								.reset();
							e.target
								.closest("tr")
								.querySelector(".fPromotionStartDate")
								.reset();
							e.target
								.closest("tr")
								.querySelector(".fPromotionEndDate")
								.reset();
							//Disable input
							e.target
								.closest("tr")
								.querySelector(".namePromotion")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".contentPromotion")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".startDatePromotion")
								.setAttribute("disabled", "disabled");
							e.target
								.closest("tr")
								.querySelector(".endDatePromotion")
								.setAttribute("disabled", "disabled");
							// Thay đổi icon mặc định
							ActionIconsElement.innerHTML = defaultActionIcons;
							//Gọi lại các hàm đề bắt sự kiện edit và remove
							app.handleEditPromotion();
							app.handleRemovePromotion();
						};
					}
				};
			});
		}
	},
	handleAddPromotion: () => {
		let btnAddEleemnt = $(".btnAddPromotion");
		if (btnAddEleemnt) {
			btnAddEleemnt.onclick = (e) => {
				let divElement = document.createElement("div");
				divElement.setAttribute(
					"class",
					"modal-addPromotion flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
				);
				divElement.innerHTML = `
        <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[800px] tablet:w-[500px] mobile:w-full overflow-hidden">
          <!-- Header -->
          <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-between items-center">
              <h3 class="text-white uppercase font-bold">Thêm khuyến mãi</h3>
              <i class="btnCancelAddPromotion text-white font-bold text-[16px] hover:cursor-pointer hover:opacity-80 far fa-times-circle"></i>
          </div>
          <!-- Content -->
              <form action="" class=" border-t-2 border-solid border-t-white shadow-lg bg-blue-500 p-3 relative">
              <div class="form-group row m-[24px_0px] items-center">
                  <label class="text-white font-bold col l-12 md-12 c-12 tablet:pb-2 mobile:pb-2" for="productName">Tên khuyến mãi</label>
                  <input name="promotionName" placeholder="Tên khuyến mãi..." class="focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5 col l-12 md-12 c-12" type="text" id="producerName">
                  <span class="error-message col l-o-4 text-red-600"></span>
              </div>
              <div class="form-group row m-[24px_0px] items-center">
                  <label class="text-white font-bold col l-12 md-12 c-12 tablet:pb-2 mobile:pb-2" for="productContent">Nội dung</label>
                  <input name="promotionContent" placeholder="Nội dung..." class="focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5 col l-12 md-12 c-12" type="text" id="producerName">
                  <span class="error-message col l-o-4 text-red-600"></span>
              </div>
              <div class="row">
              	  <div class="form-group col l-6 mb-[24px] items-center">
                  	<label class="text-white font-bold tablet:pb-2 mobile:pb-2" for="promotionStartDate">Ngày bắt đầu</label>
                  	<input name="promotionStartDate"  class="w-full focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5" type="date" id="promotionStartDate">
                  	<span class="error-message col l-o-4 text-red-600"></span>
             	  </div>
	              <div class="form-group col l-6 mb-[24px] items-center">
	                  <label class="text-white font-bold tablet:pb-2 mobile:pb-2" for="promotionEndDate">Ngày kết thúc</label>
	                  <input name="promotionEndDate"  class="w-full focus-visible:outline-none focus-visible:ring bg-white w-[280px] h-[32px] p-5 " type="date" id="promotionEndDate">
	                  <span class="error-message col l-o-4 text-red-600"></span>
	              </div>
              </div>
    
    
              <div class="row mx-1 justify-between">
                  <button type="button" class="btnSubmitAddPromotion  col l-6 btn-secondary">Lưu</button>
                  <button type="button" class="btnCancelAddPromotion col l-4 btn-cancel text-centerbtnCancel">Huỷ</button>
              </div>
          </form>
        </div>
        `;
				document.body.appendChild(divElement);

				let btnAddElement = $(".btnSubmitAddPromotion");
				let btnCancelElement = $$(".btnCancelAddPromotion");
				let data = {};
				if (btnAddElement) {
					btnAddElement.onclick = (e) => {
						//Tên KM
						let promotionName = btnAddElement
							.closest("form")
							.querySelector("input[name='promotionName']").value.trim();
						//Nội dung KM
						let promotionContent = btnAddElement
							.closest("form")
							.querySelector("input[name='promotionContent']").value.trim();
						let promotionStartDate = btnAddElement
							.closest("form")
							.querySelector("input[name='promotionStartDate']").value;
						let promotionEndDate = btnAddElement
							.closest("form")
							.querySelector("input[name='promotionEndDate']").value;


						//dữ liệu phải khác rỗng
						if (promotionName != "" && promotionContent != "") {
							app.removeModalAddPromotion(e);
							data.name = promotionName;
							data.content = promotionContent;
							data.startDate = promotionStartDate;
							data.endDate = promotionEndDate;
							formdata = new FormData();
							console.log(data);
							formdata.append("promotion", JSON.stringify(data));
							app.handleCreateAPI("/admin/promotion/", formdata).then((response) => {
								console.log(response);
								//hiện flash message
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									let body = $(".bodyListPromotion");
									if (body) {
										let tr = document.createElement("tr");
										tr.setAttribute("class", "even:bg-blue-100 odd:bg-white");
										tr.setAttribute("data-id", response.data.id);
										tr.innerHTML = `
							<td class="p-2 align-middle">
                                        <i
                                            class="text-blue-500 fas fa-plus-circle hover:cursor-pointer hover:opacity-80"></i>
                                    </td>
                                    <td class="p-2 align-middle">
                                        1
                                    </td>

                                    <td class="p-2 align-middle ">
                                        <form class="fPromotionName">
                                        	<input value= ${promotionName}
                                            class="bg-transparent focus-visible:outline-none namePromotion text-center"
                                            type="text" disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle ">
                                        <form class="fPromotionContent">
                                        	<input value=${promotionContent}
                                            class="bg-transparent focus-visible:outline-none contentPromotion text-center"
                                            type="text" disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle ">
                                        <form class="fPromotionStartDate">
                                        	<input value=${promotionStartDate}
                                            class="bg-transparent focus-visible:outline-none startDatePromotion text-center"
                                            type="text" disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle ">
                                        <form class="fPromotionEndDate">
                                        	<input value=${promotionEndDate}
                                            class="bg-transparent focus-visible:outline-none endDatePromotion text-center"
                                            type="text" disabled>
                                        </form>
                                    </td>
                                    <td class="p-2 align-middle">
                                        <div class="flex items-center justify-center actionBtn">

                                            <i 
                                                class="hover:text-blue-500 hover:cursor-pointer p-1 mr-1 fas fa-pen editPromotion"></i>
                                            <i 
                                                class="hover:text-red-500 hover:cursor-pointer p-1 mr-1 fas fa-trash removePromotion"></i>
                                        </div>
                                    </td>
							`;
										body.appendChild(tr);
										app.handleEditPromotion();
										app.handleRemovePromotion();
									}
								} else {
									//Khi status != success
								}
							});

						}
					};
				}
				if (btnCancelElement) {
					btnCancelElement.forEach((element) => {
						element.onclick = (e) => {
							app.removeModalAddPromotion(e);
						};
					});
				}
			};
		}
	},
	removeModalAddPromotion: (e) => {
		let modalElement = e.target.closest(".modal-addPromotion");
		modalElement.classList.add("fadeOut");
		setTimeout(() => {
			modalElement.remove();
		}, 300);
	},
	handleRemovePromotionProduct: () => {
		let nameProduct = $$(".removePromotionProduct");
		if (nameProduct) {
			nameProduct.forEach((element) => {
				element.onclick = () => {
					let id = element.closest("tr").dataset.id;
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
                <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
                  <!-- Header -->
                  <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                      <h3 class="text-white uppercase font-bold">Xác nhận</h3>
    
                  </div>
                  <!-- Content -->
                  <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                      <div class="p-5">
                        <p class="font-bold">Bạn chắc chắn muốn xoá sản phẩm khuyến mãi này?</p>
                      </div>
                  </div>
                  <!-- Footer -->
                  <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                      <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                      <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
                  </div>
                </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							app.handleDeleteAPI("/admin/promotion-product/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("tr").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("tr").remove();
									}, 300);
								}
							});
							divElement.remove();
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				};
			});
		}
	},
	handleAddCart: () => {
		let btnAddtoCart = $(".add-to-cart");
		if (btnAddtoCart) {
			btnAddtoCart.onclick = async () => {
				let id = btnAddtoCart.dataset.id;
				let response = await fetch("/cart/" + id, {
					method: "POST",

				});
				let data = await response.json();
				console.log(data);
				new window.FlashMessage(data.message, data.status, {
					progress: true,
					interactive: true,
					timeout: 5000,
					appear_delay: 200,
				});
			}
		}
	},
	handleViewDetailProduct: () => {

		let items = $$(".product-item");
		if (items) {

			items.forEach((element) => {
				element.onclick = () => {
					let productId = element.dataset.id;
					let productOptionId = element.dataset.productoption;
					console.log(productOptionId)
					if (productId && productOptionId) {
						window.location.assign("/product/" + productId + "?id=" + productOptionId);
					}
				}
			})
		}
	},
	handleChangeOrderState: () => {
		let StateElements = document.querySelectorAll(".orderState");
		if (StateElements) {
			StateElements.forEach(element => {
				element.onchange = async () => {
					let orderId = element.dataset.id;
					let stateId = element.value;
					if (orderId && stateId) {
						formData = new FormData();
						formData.append("stateId", parseInt(stateId));
						let response = await fetch("/admin/order/" + orderId, {
							method: "PUT",
							body: formData
						})
						let data = await response.json();
						console.log(data);
						if (data.data.state.name == 'Giao thành công') {
							element.setAttribute("disabled", "disabled");
						}
						new window.FlashMessage(data.message, data.status, {
							progress: true,
							interactive: true,
							timeout: 5000,
							appear_delay: 200,
						});
					}
				}
			})
		}
	},
	handleViewPost: () => {
		let elements = document.querySelectorAll(".posts");

		if (elements) {
			elements.forEach(element => {
				element.onclick = () => {
					console.log('a')
					let id = element.dataset.id;
					if (id) {
						location.assign("/post/" + id);
					}
				}
			})

		}
	},
	handleRemovePost: () => {
		let nameProduct = $$(".removePost");
		if (nameProduct) {
			nameProduct.forEach((element) => {
				element.onclick = () => {
					let id = element.closest("tr").dataset.id;
					let value = element
						.closest("tr")
						.querySelector(".postTitle")
						.innerText.trim();
					let divElement = document.createElement("div");
					divElement.setAttribute(
						"class",
						"modal-specifications flex fixed items-center justify-center right-0 bg-black/50 bottom-0 left-0 w-full h-full z-[99999]"
					);
					let modalHtml = `
                <div class="animate-[slideAndFade_.2s_ease-in] max-h-[calc(100vh_-_40px)] pc:w-[500px] tablet:w-[500px] mobile:w-full overflow-hidden">
                  <!-- Header -->
                  <div class="rounded-tr-lg rounded-tl-lg flex bg-blue-500 p-5 justify-center items-center">
                      <h3 class="text-white uppercase font-bold">Xác nhận</h3>
    
                  </div>
                  <!-- Content -->
                  <div class="bg-white max-h-[calc(100vh_-_200px)] flex justify-center overflow-y-auto">
                      <div class="p-5">
                        <p class="font-bold">Bạn chắc chắn muốn xoá ${value}?</p>
                      </div>
                  </div>
                  <!-- Footer -->
                  <div class="rounded-br-lg rounded-bl-lg row bg-[#f5f5f5] p-5 justify-between items-center">
                      <button class="btnAccept col l-6 toggle-modal text-white uppercase bg-green-500 w-full rounded-xl p-2 font-bold">Có</button>
                      <button class="btnDeny col l-4 toggle-modal text-white uppercase bg-red-500 w-full rounded-xl p-2 font-bold">Không</button>
                  </div>
                </div>`;
					divElement.innerHTML = modalHtml;
					document.body.appendChild(divElement);
					let btnNo = document.querySelector(".btnDeny");
					let btnYes = document.querySelector(".btnAccept");
					if (btnYes && btnNo) {
						btnYes.onclick = () => {
							app.handleDeleteAPI("/admin/post/", id).then((response) => {
								console.log(response);
								new window.FlashMessage(response.message, response.status, {
									progress: true,
									interactive: true,
									timeout: 5000,
									appear_delay: 200,
								});
								if (response.status == "success") {
									element.closest("tr").classList.add("fadeOut");
									setTimeout(() => {
										element.closest("tr").remove();
									}, 300);
								}
							});
							divElement.remove();
						};
						btnNo.onclick = () => {
							divElement.remove();
						};
					}
				};
			});
		}
	},
	handleUpdateUserRole: () => {
		let roleElement = document.querySelectorAll(".roleSelect");
		if (roleElement) {
			roleElement.forEach(element => {
				element.onchange = async () => {
					let roleId = element.value;
					let userId = element.closest("tr").dataset.id;
					let response = await fetch("/admin/user/" + userId + "/role/" + roleId, {
						method: "put"
					})
					let data = await response.json();
					console.log(data);
					new window.FlashMessage(data.message, data.status, {
						progress: true,
						interactive: true,
						timeout: 5000,
						appear_delay: 200,
					});
				}
			})
		}
	},
	handleUpdateUserEnable: () => {
		let checkbox = document.querySelectorAll(".enableCheckbox");
		if (checkbox) {
			checkbox.forEach(element => {
				element.onclick = async () => {
					let enable = 0;
					let userId = element.closest("tr").dataset.id;
					if (element.checked) enable = 1;
					let response = await fetch("/admin/user/" + userId + "/enable/" + enable, {
						method: "put"
					})
					let data = await response.json();
					console.log(data);
					new window.FlashMessage(data.message, data.status, {
						progress: true,
						interactive: true,
						timeout: 5000,
						appear_delay: 200,
					});
				}
			})
		}
	},
	handleSearchProduct: () => {
    let input = document.querySelector(".searchInput");
    let searchRes = document.querySelector(".searchResult");
    if (input && searchRes) {
      input.onkeyup = async() => {
		let name = input.value.trim();
		let response  = await fetch("/api/v1/shop?q=" + name, {
			method : "get"
		})
		let data = await response.json();
		console.log(data);
		if (data.status = "success") {
			let wrapper = document.querySelector(".resultWrap");
			if (wrapper) {
				let html = ``;
				data.data.forEach(element => {
					let productId = element.id;
					let productOptionId = element.defaultProductOptionId;
					html+= `
						<a  href="/product/${productId}?id=${productOptionId}">
                                <section class="flex bg-white p-2 hover:bg-slate-100">
                                    <img class="w-[60x] h-[60px] object-contain"
                                        src="/file/${element.thumbnail}"
                                        alt="">
                                    <div class="font-semibold">
                                        <h2>${element.formatedDefaultName}</h2>
                                        <span class="text-yellow-600">${element.formatedDefaultPriceNoDiscount}</span>
                                    </div>
                                </section>
                            </a>
					`;
				})
				wrapper.innerHTML = html;
				
			}
		}
        searchRes.classList.remove("hidden");
        
      };
      input.onblur = () => {
		if (input.value.trim() == "")
        	searchRes.classList.add("hidden");
      };
    }
  },
	start: () => {
		app.handleSearchProduct();
		//User
		app.handleUpdateUserRole();
		app.handleUpdateUserEnable();
		//post
		app.previewImg("#thumbnailPost", "#thumbnailPostImg", "#btnSelectThumbnailPost");
		app.handleRemovePost();
		app.handleViewPost();

		app.handleChangeOrderState();
		app.handleAddCart();
		app.handleViewDetailProduct();
		app.handleRemovePromotionProduct();

		//promotion
		app.handleAddPromotion();
		app.handleEditPromotion();
		app.handleRemovePromotion();
		//productOption
		app.handleAddProductOption();
		app.handleEditProductOption();
		app.handleRemoveProductOption();
		//receipt
		app.handleEditReceipt();
		app.handleRemoveReceipt();
		app.handleAddReceipt();
		app.handleAddMoreProductOptionInModal();
		app.handleRemoveProductOptionInModal();
		//producer
		app.handleEditProducer();
		app.handleRemoveProducer();
		app.handleAddProducer();
		//supplier
		app.handleEditSupplier();
		app.handleRemoveSupplier();
		app.handleAddSupplier();

		app.previewImg("#imgProduct", "#ProductImg", "#btnSelectImgProduct");
		//product
		app.handleUploadMultipleFilesProduct();
		app.handeRemoveProduct();

		app.fetchProvinces(
			"#checkoutProvince",
			"#checkoutDistrict",
			"#checkoutWard"
		);
		app.handleEditAavatar(
			".button-edit",
			".button-remove",
			"#fileAvatar",
			".avatar-profile"
		);
		app.handleSendContact();
		app.handleSaveProfile();
		app.toggleDisabledForm("#btnUpdateInfo", "#fProfile");
		app.handleCheckout("#fCheckout", ".btnCheckout");
		app.handleCountInput(".cart-detail-quantity", ".countMinus", ".countPlus");
		app.toggleModal(".modal-specifications", ".toggle-modal");
		app.scrollButton();
		app.handleFocus(
			".feedback",
			`feedback p-[3px_12px_4px_12px] font-semibold text-[#aaa] relative hover:text-black hover:after:bg-black after:w-full uppercase after:bg-[#aaa] after:content-[''] after:h-[1px] 
    after:bottom-0 after:left-0 after:absolute`,
			`feedback p-[3px_12px_4px_12px] font-semibold text-black relative hover:text-black hover:after:bg-black after:w-full uppercase after:bg-black after:content-[''] after:h-[1px] 
    after:bottom-0 after:left-0 after:absolute`
		);
		app.handleFocus(
			".rom-item",
			`rom-item hover:cursor-pointer text-center border-item rounded-lg p-1 text-[12px] font-semibold`,
			`rom-item hover:cursor-pointer border-item--active border-item text-center rounded-lg p-1 text-[12px] font-semibold`
		);
		app.handleFocus(
			".color-item",
			`color-item flex justify-center text-center border-item rounded-lg p-1 text-[12px] font-semibold`,
			`color-item flex justify-center border-item--active border-item text-center rounded-lg p-1 text-[12px] font-semibold`
		);
		app.handleLoadMoreContent("#product_info-content", ".loadmore", "768px");
		// app.showLoadingButton("#fLogin");
	},
};
app.start();

// lỗi lần trước không dùng được validation jquery là do mình
//định nghĩa $ và $$ ở script.js
