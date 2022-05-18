package com.salesmanager.shop.store.api.v2.product;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.model.catalog.product.product.instanceGroup.PersistableProductInstanceGroup;
import com.salesmanager.shop.model.entity.Entity;
import com.salesmanager.shop.store.api.exception.UnauthorizedException;
import com.salesmanager.shop.store.controller.product.facade.ProductInstanceGroupFacade;
import com.salesmanager.shop.store.controller.user.facade.UserFacade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/api/v2")
@Api(tags = { "Product instances group api" })
@SwaggerDefinition(tags = {
		@Tag(name = "Product instances group allows attaching property and images to a group of instances", description = "Manage product instances group") })
public class ProductInstanceGroupApi {

	@Autowired
	private ProductInstanceGroupFacade productInstanceGroupFacade;

	@Autowired
	private UserFacade userFacade;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value = { "/private/product/productInstanceGroup" })
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "en") })
	public @ResponseBody Entity create(@Valid @RequestBody PersistableProductInstanceGroup instanceGroup,
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language) {

		String authenticatedUser = userFacade.authenticatedUser();
		if (authenticatedUser == null) {
			throw new UnauthorizedException();
		}

		userFacade.authorizedGroup(authenticatedUser, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_CATALOGUE, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()));

		Long id = productInstanceGroupFacade.create(instanceGroup, merchantStore, language);

		return new Entity(id);

	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping(value = { "/private/product/productInstanceGroup/{id}" })
	@ApiOperation(httpMethod = "PUT", value = "Update product instance group", notes = "", produces = "application/json", response = Void.class)
	public @ResponseBody void update(@PathVariable Long id,
			@Valid @RequestBody PersistableProductInstanceGroup instance, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {

		String authenticatedUser = userFacade.authenticatedUser();
		if (authenticatedUser == null) {
			throw new UnauthorizedException();
		}

		userFacade.authorizedGroup(authenticatedUser, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_CATALOGUE, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()));

		productInstanceGroupFacade.update(id, instance, merchantStore, language);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = { "/private/product/productInstanceGroup/{id}" })
	@ApiOperation(httpMethod = "GET", value = "Get product instance group", notes = "", produces = "application/json", response = Void.class)
	public @ResponseBody void get(@PathVariable Long id, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {

		String authenticatedUser = userFacade.authenticatedUser();
		if (authenticatedUser == null) {
			throw new UnauthorizedException();
		}

		userFacade.authorizedGroup(authenticatedUser, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_CATALOGUE, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()));

		productInstanceGroupFacade.get(id, merchantStore, language);
	}

	// delete

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(value = { "/private/product/productInstanceGroup/{id}" })
	@ApiOperation(httpMethod = "DELETE", value = "Delete product instance group", notes = "", produces = "application/json", response = Void.class)
	public @ResponseBody void delete(@PathVariable Long id, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {

		String authenticatedUser = userFacade.authenticatedUser();
		if (authenticatedUser == null) {
			throw new UnauthorizedException();
		}

		userFacade.authorizedGroup(authenticatedUser, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_CATALOGUE, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()));

		productInstanceGroupFacade.delete(id, id, merchantStore);
	}

	// list
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = { "/private/product/productInstanceGroup" })
	@ApiOperation(httpMethod = "GET", value = "Delete product instance group", notes = "", produces = "application/json", response = Void.class)
	public @ResponseBody void list(
			@ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {

		String authenticatedUser = userFacade.authenticatedUser();
		if (authenticatedUser == null) {
			throw new UnauthorizedException();
		}

		userFacade.authorizedGroup(authenticatedUser, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_CATALOGUE, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()));

		//productInstanceGroupFacade.l
	}

	// add image
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = { "/private/product/productInstanceGroup/{id}/image" }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "String", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "String", defaultValue = "en") })
	public void uploadImage(@PathVariable Long id, @RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "order", required = false, defaultValue = "0") Integer position,
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language) {

		String authenticatedUser = userFacade.authenticatedUser();
		if (authenticatedUser == null) {
			throw new UnauthorizedException();
		}

		userFacade.authorizedGroup(authenticatedUser, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_CATALOGUE, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()));

		productInstanceGroupFacade.addImage(file, id, merchantStore, language);

	}

	// remove image
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = {
			"/private/product/productInstanceGroup/{id}/image/{imageId}" }, method = RequestMethod.DELETE)
	public void deleteImage(@PathVariable Long id, @PathVariable Long imageId, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {

		String authenticatedUser = userFacade.authenticatedUser();
		if (authenticatedUser == null) {
			throw new UnauthorizedException();
		}

		userFacade.authorizedGroup(authenticatedUser, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
				Constants.GROUP_ADMIN_CATALOGUE, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()));

		productInstanceGroupFacade.delete(imageId, id, merchantStore);

	}

}
