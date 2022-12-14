package com.spring.quoctrong.controllers;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.quoctrong.models.ProductOption;
import com.spring.quoctrong.models.Promotion;
import com.spring.quoctrong.models.PromotionProduct;
import com.spring.quoctrong.models.Receipt;
import com.spring.quoctrong.models.ReceiptDetail;
import com.spring.quoctrong.models.ReceiptItemDTO;
import com.spring.quoctrong.models.Invoice;
import com.spring.quoctrong.models.Order;
import com.spring.quoctrong.models.Post;
import com.spring.quoctrong.models.Producer;
import com.spring.quoctrong.models.Product;
import com.spring.quoctrong.models.ProductImage;
import com.spring.quoctrong.models.ResponseObject;
import com.spring.quoctrong.models.Role;
import com.spring.quoctrong.models.State;
import com.spring.quoctrong.models.Stock;
import com.spring.quoctrong.models.Store;
import com.spring.quoctrong.models.Supplier;
import com.spring.quoctrong.models.User;
import com.spring.quoctrong.repositories.OrderRepository;
import com.spring.quoctrong.repositories.ProductOptionRepository;
import com.spring.quoctrong.repositories.ProductRepository;
import com.spring.quoctrong.repositories.StateRepository;
import com.spring.quoctrong.repositories.StockRepository;
import com.spring.quoctrong.repositories.UserRepository;
import com.spring.quoctrong.repositories.productOptionImgRepository;
import com.spring.quoctrong.services.InvoiceService;
import com.spring.quoctrong.services.OrderService;
import com.spring.quoctrong.services.PostService;
import com.spring.quoctrong.services.ProducerService;
import com.spring.quoctrong.services.ProductOptionImgService;
import com.spring.quoctrong.services.ProductOptionService;
import com.spring.quoctrong.services.ProductService;
import com.spring.quoctrong.services.PromotionProductService;
import com.spring.quoctrong.services.PromotionService;
import com.spring.quoctrong.services.ReceiptDetailService;
import com.spring.quoctrong.services.ReceiptItemDTOService;
import com.spring.quoctrong.services.ReceiptService;
import com.spring.quoctrong.services.RoleService;
import com.spring.quoctrong.services.StateService;
import com.spring.quoctrong.services.StatisticService;
import com.spring.quoctrong.services.StockService;
import com.spring.quoctrong.services.StoreService;
import com.spring.quoctrong.services.SupplierService;
import com.spring.quoctrong.services.UserService;
import com.spring.quoctrong.services.FilesStorageService;

import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;
//States
//success, warning, error, info, bug, disabled, dark, default
//X??i c??c custom status n??y trong Response object ????? s??? d???ng flash JS
//
//N???u mu???n return model c?? quan h??? 
//th?? ph???i th??m annotation @JsonIgnore
//

@Controller
@RequestMapping("/admin")
@CrossOrigin("*")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductOptionRepository productDetailRepository;

	@Autowired
	productOptionImgRepository productOptionImgRepository;

	@Autowired
	ProductService productService;

	@Autowired
	ProductOptionService productOptionService;

	@Autowired
	FilesStorageService storageService;

	@Autowired
	ProductOptionImgService productOptionImgService;

//	Kh???i t???o d??? li???u m???u
//	@PostConstruct
//	void init() {
//		Products product1 = new Products ("iPhone 13z", "May nay ngon lam nha", "");
//		Products product2 = new Products("iPhone 13a", "May nay ngon lam nha", "");
//		ProductDetails productDetails = new ProductDetails("abc", "edf", "cedf", "asdas", "aasd", "asdas", "asda", "asdas", "sadasd", "dasda", 12313.23f, 1231.23f, product1);
//		productRepository.save(product1);
//		productRepository.save(product2);
//		productDetailRepository.save(productDetails);
//	}
	
	
	@GetMapping({ "/", ""})
	public String admin(Model model) {
		model.addAttribute("statistic", statisticService.statisticAll());
		model.addAttribute("title",	 "Admin dashboard");
		return "admin/index";
	}
	
	@GetMapping({ "product/", "product" })
	public String getAllProducts(Model model) {
		List<Product> products = productRepository.findAllByOrderByNameDesc();
		model.addAttribute("products", products);
		model.addAttribute("title",	 "Danh s??ch s???n ph???m");
		return "admin/product/listProduct";
	}

	@GetMapping(value = "/product/add")
	public String addProduct(Model model) {
		List<Producer> producers = producerService.findAllByOrderByNameAsc();
		model.addAttribute("producers", producers);
		model.addAttribute("title",	 "Th??m s???n ph???m");
		return "admin/product/addProduct";
	}

//	Th??m s???n ph???m m???i (k??m theo s???n ph???m option v?? h??nh ??nh chi ti???t)
	@PostMapping(value = "/product/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseObject> addProduct(
			@RequestPart(required = false, value = "producer") String producerStr, // th??ng tin NSX
			@RequestPart(required = false, value = "product") String productStr, // th??ng tin s???n ph???m
			@RequestPart(required = false, value = "file[]") MultipartFile[] files, // c??c h??nh ???nh c???a productOption
			@RequestPart(required = false, value = "thumbnail") MultipartFile thumbnail, // ???nh t?????ng rt??ng c???a product
			@RequestPart(required = false, value = "productOption") String productOptionStr) {// th??ng tin s???n ph???m
																								// options

//		Chuy???n ?????i String producer sang Model producer
		Producer producer = producerService.convertToModel(producerStr);
//		Chuy???n ?????i String product sang Model product
		Product product = productService.convertToModel(productStr);
//		Chuy???n ?????i String productOption sang Model productOption
		ProductOption productOption = productOptionService.convertToModel(productOptionStr);

//		Product kh??ng tr??ng t??n
		if (productService.existsByName(product.getName())) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResponseObject("error", "S???n ph???m ???? t???n t???i!", ""));
		}
		if (producerService.existById(producer.getId())) {
			// g??n id producer cho product
			product.setProducer(producer);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("error", "Kh??ng t??m th???y nh?? s???n xu???t!", ""));
		}
//
		try {
			// g??n product cho productOption
			productOption.setProduct(product);
		} catch (Exception e) {
			throw new RuntimeException();
		}

		// X??? ly upload file product thumbnail
		if (thumbnail != null) {
			try {

				String generatedFileName = storageService.save(thumbnail);
//				L??u t??n file v??o database
				product.setThumbnail(generatedFileName);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new ResponseObject("error", "Vui l??ng ch???n ???nh hi???n th???!", ""));
		}

//		L??u product v??o database
		productRepository.save(product);

//		Sau khi ???? l??u product v??o database, th?? c?? th??? l??u productOption v??o, v?? l??c n??y id product ???? c??
		productDetailRepository.save(productOption);

//		X??? ly upload file product option imgs
		if (files != null) {
			try {
				Arrays.asList(files).stream().forEach(file -> {
					String generatedFileName = storageService.save(file);
					ProductImage productImage = new ProductImage();
					productImage.setProductOption(productOption);
					productImage.setUrl(generatedFileName);
//					Sau khi ???? l??u productOption v??o database, th?? c?? th??? l??u productOptionImg v??o, v?? l??c n??y id productOption ???? c??
					productOptionImgRepository.save(productImage);
				});
			} catch (Exception e) {

				throw new RuntimeException();
			}
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new ResponseObject("error", "Vui l??ng ch???n ???nh chi ti???t!", ""));
		}

		return ResponseEntity.ok().body(new ResponseObject("success", "Th??m s???n ph???m th??nh c??ng!", productOption));

	}

	@GetMapping("/product/update/{id}")
	public String updateProduct(@PathVariable int id, Model model) {
		List<Producer> producers = producerService.findAllByOrderByNameAsc();
		Optional<Product> productEdit = productRepository.findById(id);
		model.addAttribute("producers", producers);
		model.addAttribute("title", "C???p nh???t s???n ph???m");
		productEdit.ifPresent(product -> model.addAttribute("product", product));
		;
		return "admin/product/editProduct";
	}

	@PostMapping("/product/update")
	public String updateProduct(Product newProduct,
			@RequestPart(required = false, value = "imageProduct") MultipartFile thumbnail,
			RedirectAttributes redirectAttributes) {
//		L??u gi?? tr??? thumbnail c??
		String oldThumbnail = productService.findProductById(newProduct.getId()).get().getThumbnail();

		// X??? l?? update product tr??ng t??n v???i product ???? t???n t???i
		for (Product product : productRepository.findAll()) {
			if (product.getName().equals(newProduct.getName()) && product.getId() != newProduct.getId()) {
				System.out.println(product.getName() + newProduct.getName());
				redirectAttributes.addFlashAttribute("error", "T??n s???n ph???m ???? t???n t???i! Vui l??ng s??? d???ng t??n kh??c.");
				return "redirect:/admin/product/update/" + newProduct.getId();
			}

		}

//		N???u t???n t???i file upload trong input
		if (thumbnail != null && !thumbnail.isEmpty()) {
			try {

				String generatedFileName = storageService.save(thumbnail);
//				L??u t??n file v??o database
				newProduct.setThumbnail(generatedFileName);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		} else {
			// N???u kh??ng upload thumbnail..
			newProduct.setThumbnail(oldThumbnail);

		}
		productRepository.save(newProduct);
		redirectAttributes.addFlashAttribute("success", "C???p nh???t s???n ph???m th??nh c??ng!");
		return "redirect:/admin/product";
	}

	@DeleteMapping("/product/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable int id) {
		Optional<Product> foundProduct = productService.findProductById(id);
		String message = new String();
		String status = new String();
		Product product = new Product();
//		Tr???ng th??i http
		HttpStatus status2 = HttpStatus.OK;
//		N???u t??m th???y s???n ph???m
		if (foundProduct.isPresent()) {
			product = foundProduct.get();
			productRepository.deleteById(id);
			status = "success";
			message = "Xo?? " + product.getName() + " Th??nh c??ng!";
		} else {
			status = "error";
			message = "Kh??ng t??m th???y s???n ph???m!";
			status2 = HttpStatus.NOT_FOUND;
		}

		return ResponseEntity.status(status2)

				.body(new ResponseObject(status, message, ""));

//		return "admin/index";
	}

//	PRODUCT OPTION MANGER

	@GetMapping({ "/productOption/", "/productOption" })
	public String listProductOptionByProductId(Model model, @RequestParam(value = "productId") int id,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.findProductById(id)
					.orElseThrow(() -> new Exception("Kh??ng t??m th???y s???n ph???m!"));
			Set<ProductOption> productOptions = product.getProductOptions();
			model.addAttribute("productOptions", productOptions);
			model.addAttribute("product", product);
			model.addAttribute("title", "Phi??n b???n s???n ph???m");
			return "admin/product_option/list";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/product";
		}
	}

	@GetMapping(value = { "/productOption/add", "/productOption/add/" })
	public String addProductOptionByProductId(Model model, @RequestParam(value = "productId") int id,
			RedirectAttributes redirectAttributes) {
		try {
			Product product = productService.findProductById(id)
					.orElseThrow(() -> new Exception("Kh??ng t??m th???y s???n ph???m!"));
			model.addAttribute("title", "Th??m m???i");
			model.addAttribute("product", product);
			return "admin/product_option/add";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/productOption?productId=" + id;
		}
	}

	@PostMapping(value = { "/productOption/add", "productOption/add/" }, consumes =

	{ MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })

	@ResponseBody
	public ResponseEntity<ResponseObject> addProductOptionByProductId(
			@RequestParam(required = false, value = "productId") int productId,
			@RequestPart(required = false, value = "file[]") MultipartFile[] images,
			@RequestPart(required = false, value = "productOption") String productOptionStr) {

		try {
			Product product = productService.findProductById(productId)
					.orElseThrow(() -> new Exception("Kh??ng t??m th???y s???n ph???m!"));
			ProductOption productOption = productOptionService.convertToModel(productOptionStr);
			productOption.setProduct(product);
			productOption = productOptionService.save(productOption);
			if (images != null) {
				for (MultipartFile image : images) {
					String generatedName = storageService.save(image);
					ProductImage productImage = new ProductImage(generatedName, productOption);
					productOptionImgService.save(productImage);
				}
			} else {
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(new ResponseObject("error", "Vui l??ng ch???n ???nh chi ti???t!", ""));
			}
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResponseObject("success", "Th??m th??nh c??ng!", productOption));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", e.getMessage(), ""));
		}

	}

	@PutMapping({ "/productOption/{id}", "/productOption/{id}/" })
	@ResponseBody
	public ResponseEntity<ResponseObject> updateProductOption(@PathVariable(required = false, value = "id") int id,
			@RequestPart(required = false, value = "productOption") String produtOptionStr,
			@RequestPart(required = false, value = "file[]") MultipartFile[] images,
			@RequestParam(required = false, value = "imageDeletedId") int[] imageDeletedIds) {
		try {
			System.out.println(produtOptionStr);
			ProductOption newProductOption = productOptionService.convertToModel(produtOptionStr);
			ProductOption updatedProductOption = productOptionService.findById(id)
					.orElseThrow(() -> new Exception("M?? s??? kh??ng t???n t???i!"));
			// C???p nh???t l???i c??c th??ng tin c???a s???n ph???m
			System.out.println(newProductOption);
			updatedProductOption.setAllSpecifications(newProductOption.getAllSpecifications());
			updatedProductOption.setColor(newProductOption.getColor());
			updatedProductOption.setCostPrice(newProductOption.getCostPrice());
			updatedProductOption.setCpu(newProductOption.getCpu());
			updatedProductOption.setFrontCamera(newProductOption.getFrontCamera());
			updatedProductOption.setGpu(newProductOption.getGpu());
			updatedProductOption.setMonitor(newProductOption.getMonitor());
			updatedProductOption.setPin(newProductOption.getPin());
			updatedProductOption.setRam(newProductOption.getRam());
			updatedProductOption.setRearCamera(newProductOption.getRearCamera());
			updatedProductOption.setRom(newProductOption.getRom());
			updatedProductOption.setSellingPrice(newProductOption.getSellingPrice());
			productOptionService.save(updatedProductOption);
			// Xo?? c??c ???nh ???? ch???n
			if (imageDeletedIds != null && imageDeletedIds.length > 0) {
				for (int imageId : imageDeletedIds) {
					if (productOptionImgService.existsById(imageId)) {
						productOptionImgService.deleteById(imageId);
					}
				}
			}
			// Th??m c??c ???nh m???i v??o
			if (images != null  && images.length > 0) {
				for (MultipartFile imageFile : images) {
					String generatedName = storageService.save(imageFile);
					ProductImage productImage = new ProductImage(generatedName, updatedProductOption);
					productOptionImgService.save(productImage);
				}
			}

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("success", "C???p nh???t th??nh c??ng!", updatedProductOption));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("error", e.getMessage(), ""));
		}

	}

	@DeleteMapping({ "/productOption/{id}", "/productOption/{id}/" })
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteProductOption(@PathVariable(required = false, value = "id") int id) {
		try {
			ProductOption productOption = productOptionService.findById(id)
					.orElseThrow(() -> new Exception("M?? s??? kh??ng t???n t???i!"));
			productOptionService.delete(productOption);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Xo?? th??nh c??ng!", ""));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("error", e.getMessage(), ""));
		}
	}

//  PRODUCER MANAGER
	@Autowired
	ProducerService producerService;

	@GetMapping({ "/producer", "/producer/" })
	public String listProducer(Model model) {
		List<Producer> producers = producerService.findAllByOrderByNameAsc();
		model.addAttribute("producers", producers);
		model.addAttribute("title", "Nh?? s???n xu???t");
		return "admin/producer/list";
	}

	@PostMapping(value = { "/producer", "/producer/" }, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseObject> addProducer(@RequestPart(value = "producer") String producerStr) {
		Producer producer = producerService.convertToModel(producerStr);
		ResponseObject responseObject = producerService.add(producer);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);

	}

	@PutMapping(value = { "/producer/{id}", "/producer/{id}/" }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<ResponseObject> updateProducer(@RequestPart(value = "producer") String producerStr,
			@PathVariable(value = "id") int id) {
		Producer producer = producerService.convertToModel(producerStr);
		// g??n id cho producer
		producer.setId(id);
		ResponseObject responseObject = producerService.update(producer);
		return ResponseEntity.status(HttpStatus.OK).body(responseObject);
	}

	@DeleteMapping(value = { "/producer/{id}", "/producer/{id}/" })
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteProducer(@PathVariable(value = "id") int id) {
		ResponseObject responseObject = producerService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(responseObject);
	}

	// SUPPLIER MANAGER
	@Autowired
	SupplierService supplierService;

	@GetMapping({ "/supplier", "/supplier" })
	public String listSupplier(Model model) {
		List<Supplier> suppliers = supplierService.findAllByOrderByNameAsc();
		model.addAttribute("suppliers", suppliers);
		model.addAttribute("title", "Nh?? cung c???p");
		return "admin/supplier/list";
	}

	@PostMapping(value = { "/supplier", "/supplier/" }, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseObject> addSupplier(@RequestPart(value = "supplier") String supplierStr) {
		Supplier supplier = supplierService.convertToModel(supplierStr);
		ResponseObject responseObject = supplierService.add(supplier);
		return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);

	}

	@PutMapping(value = { "/supplier/{id}", "/supplier/{id}/" }, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseBody
	public ResponseEntity<ResponseObject> updateSupplier(@RequestPart(value = "supplier") String supplierStr,
			@PathVariable(value = "id") int id) {
		Supplier supplier = supplierService.convertToModel(supplierStr);
		// g??n id cho producer
		supplier.setId(id);
		ResponseObject responseObject = supplierService.update(supplier);
		return ResponseEntity.status(HttpStatus.OK).body(responseObject);
	}

	@DeleteMapping(value = { "/supplier/{id}", "/supplier/{id}/" })
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteSupplier(@PathVariable(value = "id") int id) {
		ResponseObject responseObject = supplierService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(responseObject);
	}

	// RECEIPT MANAGER (Hoa don nen co the khong can EDIT)
	@Autowired
	ReceiptService receiptService;
	@Autowired
	StoreService storeService;
	@Autowired
	ReceiptItemDTOService receiptItemDTOService;
	@Autowired
	ReceiptDetailService receiptDetailService;
	@Autowired
	StockService stockService;

	@GetMapping({ "/receipt", "/receipt" })
	public String listReceipt(Model model) {
		List<Receipt> receipts = receiptService.findAllByOrderByStoreAsc();
		List<Store> stores = storeService.findAllByOrderByNameAsc();
		List<Supplier> suppliers = supplierService.findAllByOrderByNameAsc();
		List<ProductOption> productOptions = productOptionService.findAllByOrderByProductAsc();
		// ????a c??c model ra view
		model.addAttribute("receipts", receipts);
		model.addAttribute("stores", stores);
		model.addAttribute("suppliers", suppliers);
		model.addAttribute("title", "????n nh???p h??ng");
		model.addAttribute("productOptions", productOptions);
		return "admin/receipt/list";
	}

	@GetMapping({ "/receipt/{id}", "/receipt/{id}/" })
	public String receiptDetail(Model model, @PathVariable(value = "id") int id) {
		try {
			Receipt receipt = receiptService.findById(id).get();
			model.addAttribute("title", "Chi ti???t ????n nh???p");
			model.addAttribute("receipt", receipt);
			return "admin/receipt/detail";
		} catch (Exception e) {
			return "admin/receipt";
		}

	}

	@PostMapping(value = { "/receipt", "/receipt/" }, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@ResponseBody
	public ResponseEntity<ResponseObject> addReceipt(@RequestParam(value = "storeId") Integer storeId,
			@RequestParam(value = "supplierId") Integer supplierId, @RequestParam(value = "items") String[] itemsStr) {
		// handle l???i ??? trong h??m try, n???u c?? l???i s??? h???ng ??? catch
		try {
			Receipt receipt = receiptService.add(storeId, supplierId);
			for (String item : itemsStr) {
				if (!item.equals("null") && !item.equals("")) {// "null" ??? ????y l?? m??nh t???o ra b??n JS ????? @requestparam
																// items
																// l?? m???t m???ng
					ReceiptItemDTO receiptItemDTO = receiptItemDTOService.convertToModel(item);
					ResponseObject responseObject = receiptDetailService.add(receipt, receiptItemDTO, true);
				}
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", e.getMessage(), ""));
		}

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseObject("success", "Th??m ????n nh???p th??nh c??ng!", ""));

	}

	@DeleteMapping(value = { "/receipt/{id}", "/receipt/{id}/" })
	@ResponseBody
	public ResponseEntity<ResponseObject> deleteReceipt(@PathVariable(value = "id") int id) {
		ResponseObject responseObject = new ResponseObject();
		try {// X??? l?? ngo??i l???, l?? s??? ???????c ????a v??o catch
			responseObject = receiptService.deleteById(id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", e.getMessage(), ""));
		}
		return ResponseEntity.status(HttpStatus.OK).body(responseObject);
	}
	
	
	//PROMOTION MANAGER
	
	@Autowired
	PromotionService promotionService;
	@GetMapping({"/promotion", "promotion/"})
	public String listPromotion(Model model) {
		List<Promotion> promotions = promotionService.findAllByOrderByNameAsc();
		model.addAttribute("promotions", promotions);
		model.addAttribute("title", "Khuy???n m??i");
		return "admin/promotion/list";
	}
	
	@PostMapping(value = {"/promotion", "/promotion/"})
	@ResponseBody
	public ResponseEntity<ResponseObject> addPromotion(@RequestPart(value = "promotion") String promotionStr) {
		try {
			Promotion promotion = promotionService.convertToModel(promotionStr);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "Th??m khuy???n m??i th??nh c??ng!", promotionService.save(promotion)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("error", e.getMessage(), ""));
		}
	}
	
	@PutMapping(value = {"/promotion/{id}", "/promotion/{id}/"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
	@ResponseBody()
	public ResponseEntity<ResponseObject> updatePromotion(@PathVariable(value = "id") Integer id,
			@RequestPart(value = "promotion") String promotionStr) {
		try {
			Promotion promotion = promotionService.convertToModel(promotionStr);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject("success", "C???p nh???t khuy???n m??i th??nh c??ng!", promotionService.update(id, promotion)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseObject("error", e.getMessage(), ""));
		}
	}
	
	@DeleteMapping(value = {"/promotion/{id}", "/promotion/{id}/"})
	@ResponseBody
	public ResponseEntity<ResponseObject> deletePromotion(@PathVariable(value = "id") Integer id) {
		try {
			promotionService.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Xo?? khuy???n m??i th??nh c??ng!", ""));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", e.getMessage(), ""));
		}
	}
	
	
	@Autowired
	PromotionProductService promotionProductService;
	//PROMOTION PRODUCT
	@GetMapping({"/promotion-product", "/promotion-product/"})
	public String listPromotionProduct(Model model) {
		List<PromotionProduct> promotionProducts = promotionProductService.findAll();
		model.addAttribute("promotionProducts", promotionProducts);
		model.addAttribute("title", "S???n ph???m khuy???n m??i");
		return "admin/promotion/product";
	}
	
	@GetMapping({"/promotion-product/add","/promotion-product/add/"})
	public String addPromotionProduct(Model model) {
		List<Product> products = productService.findAll();
		List<Promotion> promotions = promotionService.findAll();
		PromotionProduct promotionProduct = new PromotionProduct();
		model.addAttribute("products", products);
		model.addAttribute("title", "Th??m s???n ph???m khuy???n m??i");
		model.addAttribute("promotions", promotions);
		model.addAttribute("promotionProduct", promotionProduct);
		return "admin/promotion/add-product";
	}
	
	@PostMapping({"/promotion-product/add", "promotion-product/add/"})
	public String addPromotionProduct(@ModelAttribute(value = "promotionProduct") 
	PromotionProduct promotionProduct, RedirectAttributes redirectAttributes) {
		
		try {
			promotionProductService.save(promotionProduct);
			redirectAttributes.addFlashAttribute("success", "Th??m s???n ph???m khuy???n m??i th??nh c??ng!");
			return "redirect:/admin/promotion-product";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/promotion-product/add";
		}	
	}
	
	@GetMapping({"/promotion-product/{id}", "/promotion-product/{id}/"})
	public String updatePromotionProduct(Model model, @PathVariable(value = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			PromotionProduct promotionProduct = promotionProductService.findById(id);
			List<Promotion> promotions = promotionService.findAll();
			List<Product> products = productService.findAll();
			model.addAttribute("promotionProduct", promotionProduct);
			model.addAttribute("promotions", promotions);
			model.addAttribute("products", products);
			return "admin/promotion/edit-product";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/promotion-product";
		}			
	}
	
	@PostMapping({"/promotion-product/{id}", "promotion-product/{id}/"})
	public String updatePromotionProduct(@ModelAttribute(value = "promotionProduct") 
	PromotionProduct promotionProduct, RedirectAttributes redirectAttributes) {		
		try {
			promotionProductService.update(promotionProduct);
			redirectAttributes.addFlashAttribute("success", "C???p nh???t s???n ph???m khuy???n m??i th??nh c??ng!");
			return "redirect:/admin/promotion-product";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/promotion-product/" + promotionProduct.getId();
		}	
	}
	
	@DeleteMapping({"/promotion-product/{id}", "/promotion-product/{id}/"})
	@ResponseBody
	public ResponseEntity<ResponseObject> deletePromotionProduct(@PathVariable(value = "id") Integer id) {
		try {
			promotionProductService.deleteById(id); 
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Xo?? s???n ph???m khuy???n m??i th??nh c??ng!", ""));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", e.getMessage(), ""));
		}
	}
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private StateService stateService;
	//ORDER LIST
	@GetMapping({"/order", "/order/"})
	public String orderList(Model model) {
		List<State> states = stateService.findAll();
		List<Order> orders = orderService.findAll();
		model.addAttribute("orders", orders);
		model.addAttribute("states", states);
		model.addAttribute("title", "????n ?????t");
		return "admin/order/list";
	}
	
	@PutMapping({"/order/{id}", "/order/{id}/"})
	public ResponseEntity<ResponseObject> changeOrderState(@PathVariable(value = "id") Integer orderId, @RequestParam(value = "stateId") Integer StaetId) {
		try {
			Order order = orderService.changeState(orderId, StaetId);
			if (order.getState().getName().equals("Giao th??nh c??ng")) {
				invoiceService.createInvoice(order);
			}
			return ResponseEntity.ok().body(new ResponseObject("success", "C???p nh???t tr???ng th??i ????n h??ng th??nh c??ng!", order));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new ResponseObject("error", e.getMessage(), ""));
		}
		
	}
	
	//Invoice
	@Autowired
	InvoiceService invoiceService;
	@GetMapping({"/invoice", "/invoice/"})
	public String invoiceList(Model model) {
		List<State> states = stateService.findAll();
		List<Invoice> invoices = invoiceService.findAll();
		model.addAttribute("invoices", invoices);
		model.addAttribute("states", states);
		model.addAttribute("title", "Ho?? ????n");
		return "admin/invoice/list";
	}
	
	
	@GetMapping({"/invoice/{id}", "/invoice/{id}/"})
	public String invoiceDetail(Model model, @PathVariable(value = "id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			Invoice invoice = invoiceService.findById(id).orElseThrow(() -> new Exception("Kh??ng t??m th???y ho?? ????n!"));
			model.addAttribute("invoice", invoice);
			return "admin/invoice/detail";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/invoice";
		}
		
	}
	
	
	
	//STORE
	@GetMapping({"/store", "/store/"})
	public String store(Model model) {
		List<Store> stores = storeService.findAllByOrderByNameAsc();
		model.addAttribute("stores", stores);
		model.addAttribute("title", "C???a h??ng");
		return "admin/store/list";
	}
	
	
	//STATISTIC
	@Autowired
	StatisticService statisticService;
	@GetMapping({"/statistic"})
	public String statistic(Model model) {
		model.addAttribute("title", "Th???ng k??");
		model.addAttribute("statistic", statisticService.statisticAll());
		return "admin/statistic/index";
	}
	
	
	//POST
	@Autowired
	PostService postService;
	@GetMapping({"/post", "/post/"})
	public String listPost(Model model) {
		List<Post> posts = postService.findAll();
		model.addAttribute("posts", posts);
		model.addAttribute("title", "B??i vi???t");
		return "admin/post/list";
	}
	
	@GetMapping({"/post/add", "/post/add/"})
	public String addPost(Model model, RedirectAttributes redirectAttributes) {
		Post post = new Post();
		model.addAttribute("post", post);
		model.addAttribute("title", "Th??m b??i vi???t");
		return "admin/post/add";
	}
	
	@PostMapping({"/post/add", "/post/add/"})
	public String addPost(@ModelAttribute(value = "post") Post post, RedirectAttributes redirectAttributes, @RequestPart(value = "file") MultipartFile file) {
		try {
			postService.save(post, file);
			redirectAttributes.addFlashAttribute("success", "Th??m b??i vi???t th??nh c??ng!");
			return "redirect:/admin/post";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/post/add";
		}
	}
	
	@GetMapping({"/post/{id}", "/post/{id}/"})
	public String updatePost(Model model, RedirectAttributes redirectAttributes,@PathVariable(value = "id") Integer id) {
		try {
			Post post = postService.findById(id).orElseThrow(() -> new Exception("B??i vi???t kh??ng t???n t???i!"));
			model.addAttribute("post", post);
			model.addAttribute("title", "Ch???nh s???a");
			return "admin/post/edit";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/post";
		}
	}
	
	@PostMapping({"/post/update", "/post/update/"})
	public String updatePost(@ModelAttribute(value = "post") Post post, RedirectAttributes redirectAttributes, @RequestPart(value = "file") MultipartFile file) {
		try {
			postService.update(post, file);
			redirectAttributes.addFlashAttribute("success", "C???p nh???t b??i vi???t th??nh c??ng!");
			return "redirect:/admin/post";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/admin/post/" + post.getId();
		}
	}
	
	
	@DeleteMapping({"/post/{id}", "/post/{id}/"})
	@ResponseBody
	public ResponseEntity<?> deletePost(@PathVariable(value = "id") Integer id) {
		try {
			postService.deleteById(id);
			return ResponseEntity.ok().body(new ResponseObject("success", "Xo?? b??i vi???t th??nh c??ng!", ""));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new ResponseObject("error", e.getMessage(), ""));
		}
	}
	
	
	//USER
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@GetMapping({"/user", "/user/"})
	public String lstUser(Model model) {
		List<User> users = userService.findAll();
		List<Role> roles = roleService.findAll();
		model.addAttribute("title", "T??i kho???n");
		model.addAttribute("users", users);
		model.addAttribute("roles", roles);
		return "admin/user/list";
	}
	
	@PutMapping({"/user/{userId}/role/{roleId}"})
	@ResponseBody
	public ResponseEntity<?> updateRole(@PathVariable("userId") Integer userId, @PathVariable(value = "roleId") Integer roleId) {
		
		try {
			userService.updateRole(userId, roleId);
			return ResponseEntity.ok().body(new ResponseObject("success", "C???p nh???t vai tr?? th??nh c??ng!", ""));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new ResponseObject("error", e.getMessage(), ""));
		}
	}
	
	@PutMapping({"/user/{userId}/enable/{enable}"})
	@ResponseBody
	public ResponseEntity<?> updateEnable(@PathVariable("userId") Integer userId, @PathVariable(value = "enable") Integer enable) {
		
		try {
			userService.updateEnable(userId, enable);
			return ResponseEntity.ok().body(new ResponseObject("success", "C???p nh???t tr???ng th??i ng?????i d??ng th??nh c??ng!", ""));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new ResponseObject("error", e.getMessage(), ""));
		}
	}

}
