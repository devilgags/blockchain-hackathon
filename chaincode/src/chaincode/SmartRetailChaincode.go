package main

import (
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type SmartRetailChaincode struct {
}

// Master to store required details of all entities in a blockchain
type Master struct {
	AllUsers            []string
	AllPackedProducts     []string
	AllRawProducts         []string
	AllPurchases []string

	
}

// User detailed structure
type User struct {
	UserId   string
	UserName string
	PhoneNo string
	Type   string
	Purpose string
	PurchaseId  []string
}

// RawProducts detailed structure
type RawProduct struct {
	ProductId                 string
	Barcode                   string
	ProductName               string
	Price                     string
	Discount                  string
	NutritionFacts            string
	ReadyDate                 string
	Status                    string
	TransportStartDate        string
	TransportEndDate          string
	InventoryStartDate        string
	Type                      string

}

// PackedProducts detailed structure
type PackedProduct struct {
	ProductId                 string
	ProductName               string
	Price                     string
	Discount                  string
	NutritionFacts            string
	Type                      string
	Status                    string
	Weight                    string

}



// Purchase detailed structure
type Purchase struct {
	PurchaseId    string
	UserId        string
	ProductId     string
	ProductName   string
	Price         string
	PricePaid     string
	PurchaseDate          string
	Status        string
	Discount    string
}

const (
	CART             = "CART"
	COMPLETE         ="COMPLETE"

)


func main() {
	err := shim.Start(new(SmartRetailChaincode))
	if err != nil {

	}
}

func (t *SmartRetailChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {

	fmt.Println("########### example_cc Init ###########")
	var err error

	masterContract := Master{}
	newMasterContractByte, err := json.Marshal(masterContract)
	if err != nil {
		return shim.Error("Error occured")
	}
	err = stub.PutState("MASTER", newMasterContractByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)

}

// Invoke function to call user defined methods on blockchain
func (t *SmartRetailChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()

	if function == "addUser" {
		return t.addUser(stub, args)
	}
	if function == "getSimilarProducts"{
		return t.getSimilarProducts(stub,args)
	}
	if function == "getUserById" {
		return t.getUserById(stub, args)
	}
	if function == "insertPackedProducts" {
		return t.insertPackedProducts(stub, args)
	}
	if function == "insertRawProducts" {
		return t.insertRawProducts(stub, args)
	}
	if function == "getAllRawProducts" {
		return t.getAllRawProducts(stub, args)
	}
	if function == "getProductByProductId" {
		return t.getProductByProductId(stub, args)
	}
	if function == "getProductByBarcode" {
		return t.getProductByBarcode(stub, args)
	}

	if function == "updateRawProducts" {
		return t.updateRawProducts(stub, args)
	}

	if function == "addToCart" {
		return t.addToCart(stub, args)
	}

	if function == "removeFromCart" {
		return t.removeFromCart(stub, args)
	}
	if function == "getProductsInCartByUserId" {
		return t.getProductsInCartByUserId(stub, args)
	}
	if function == "makePurchase" {
		return t.makePurchase(stub, args)
	}
	if function == "getPurchasesByuserId" {
		return t.getPurchasesByuserId(stub, args)
	}
	if function == "getPurchasesByPurchaseId" {
		return t.getPurchasesByPurchaseId(stub, args)
	}
	return shim.Success(nil)
}

func (t *SmartRetailChaincode) addUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error
	fmt.Println("########### example_cc Invoke add user")
	newUser := User{
		UserId:   args[0],
		UserName: args[1],
		PhoneNo:   args[2],
		Type:     args[3],
	    Purpose:  args[4]}

	newUserByte, err := json.Marshal(newUser)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(newUser.UserId, newUserByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	masterByte, err := stub.GetState("MASTER")
	if err != nil {
		return shim.Error(err.Error())
	}

	master := Master{}
	masterByteError := json.Unmarshal(masterByte, &master)
	if masterByteError != nil {
		return shim.Error(err.Error())
	}
	allUsers := master.AllUsers
	allUsers = append(allUsers, newUser.UserId)
	master.AllUsers = allUsers

	newMasterByte, err := json.Marshal(master)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState("MASTER", newMasterByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

func (t *SmartRetailChaincode) getUserById(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error

	UserBytes, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(UserBytes)

}
func (t *SmartRetailChaincode) insertPackedProducts(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	fmt.Println("########### example_cc insert Packed product ###########")

	var err error

	newPacked := PackedProduct{
		ProductId:  args[0],
		ProductName:   args[1],
		Price:     args[2],
		Discount:    args[3],
		NutritionFacts:   args[4],
		Status:      args[5],
		Type:   args[6],
	    Weight:  args[7]}
	newProductByte, err := json.Marshal(newPacked)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(newPacked.ProductId, newProductByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	masterByte, err := stub.GetState("MASTER")
	if err != nil {
		return shim.Error(err.Error())
	}

	master := Master{}
	masterByteError := json.Unmarshal(masterByte, &master)
	if masterByteError != nil {
		return shim.Error(err.Error())
	}
	allProducts := master.AllPackedProducts
	allProducts = append(allProducts, newPacked.ProductId)
	master.AllPackedProducts = allProducts

	newMasterByte, err := json.Marshal(master)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState("MASTER", newMasterByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}
func (t *SmartRetailChaincode) insertRawProducts(stub shim.ChaincodeStubInterface, args []string) pb.Response {


   	fmt.Println("########### insert raw product ###########with purchaseId"+args[1])

	var err error

	newRaw := RawProduct{
		ProductId:  args[0],
		ProductName:   args[1],
		Price:     args[2],
		Discount:    args[3],
		Barcode:    args[4],
		NutritionFacts:   args[5],
		ReadyDate:   args[6],
		TransportStartDate: args[7],
		Status: args[8],
		Type: args[9],
		TransportEndDate: args[10],
		InventoryStartDate: args[11]}
	newProductByte, err := json.Marshal(newRaw)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(newRaw.Barcode, newProductByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	masterByte, err := stub.GetState("MASTER")
	if err != nil {
		return shim.Error(err.Error())
	}

	master := Master{}
	masterByteError := json.Unmarshal(masterByte, &master)
	if masterByteError != nil {
		return shim.Error(err.Error())
	}
	allProducts := master.AllRawProducts
	allProducts = append(allProducts, newRaw.Barcode)
	master.AllRawProducts = allProducts

	newMasterByte, err := json.Marshal(master)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState("MASTER", newMasterByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

func (t *SmartRetailChaincode) getAllRawProducts(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error

	masterContractBytes, err := stub.GetState("MASTER")
	if err != nil {
		return shim.Error(err.Error())
	}

	masterContract := Master{}
	errUnmarshal := json.Unmarshal(masterContractBytes, &masterContract)
	if errUnmarshal != nil {
		return shim.Error(errUnmarshal.Error())
	}

	allProducts := masterContract.AllRawProducts

	size := len(allProducts)
	var allProductsArray []RawProduct
	var i int
	for i = 0; i < size; i++ {
		productByte, err := stub.GetState(allProducts[i])
		if err != nil {
			return shim.Error(err.Error())
		}

		product := RawProduct{}
		errorUnmarshal := json.Unmarshal(productByte, &product)
		if errorUnmarshal != nil {
			return shim.Error(errorUnmarshal.Error())
		}
		allProductsArray = append(allProductsArray, product)
	}

	allProductsArrayByte, err := json.Marshal(allProductsArray)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(allProductsArrayByte)
}

func (t *SmartRetailChaincode) getAllPackedProducts(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error

	masterContractBytes, err := stub.GetState("MASTER")
	if err != nil {
		return shim.Error(err.Error())
	}

	masterContract := Master{}
	errUnmarshal := json.Unmarshal(masterContractBytes, &masterContract)
	if errUnmarshal != nil {
		return shim.Error(errUnmarshal.Error())
	}

	allProducts := masterContract.AllPackedProducts

	size := len(allProducts)
	var allProductsArray []PackedProduct
	var i int
	for i = 0; i < size; i++ {
		productByte, err := stub.GetState(allProducts[i])
		if err != nil {
			return shim.Error(err.Error())
		}

		product := PackedProduct{}
		errorUnmarshal := json.Unmarshal(productByte, &product)
		if errorUnmarshal != nil {
			return shim.Error(errorUnmarshal.Error())
		}
		allProductsArray = append(allProductsArray, product)
	}

	allProductsArrayByte, err := json.Marshal(allProductsArray)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(allProductsArrayByte)
}

func (t *SmartRetailChaincode) getProductByProductId(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error
	fmt.Println("###########getProductByProductId###########"+args[0])

	productBytes, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(productBytes)

}
func (t *SmartRetailChaincode) getProductByBarcode(stub shim.ChaincodeStubInterface, args []string) pb.Response {

 fmt.Println("###########getProductByBarcode###########"+args[0])
	var err error

	productBytes, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(productBytes)

}

func (t *SmartRetailChaincode) updateRawProducts(stub shim.ChaincodeStubInterface, args []string) pb.Response {

   fmt.Println("###########updaterawproduct########### with barcode"+args[4])
	var err error

	productBytes, err := stub.GetState(args[4])
	if err != nil {
		return shim.Error(err.Error())
	}

	product := RawProduct{}
	errUnmarshal := json.Unmarshal(productBytes, &product)
	if errUnmarshal != nil {
		return shim.Error(errUnmarshal.Error())
	}
	productId := (args[0])
	productName := (args[1])
	price := (args[2])
	discount := (args[3])
	barcode := (args[4])
	nutritionFacts := (args[5])
	readyDate := (args[6])
	status := (args[7])
	transportStartDate := (args[8])
	transportEndDate := (args[9])
	inventoryStartDate := (args[10])
	productType := (args[11])


	product.ProductId = productId
	product.ProductName = productName
	product.Price = price
	product.Discount = discount
	product.Barcode = barcode
	product.NutritionFacts = nutritionFacts
	product.ReadyDate = readyDate
	product.Status = status
	product.TransportStartDate = transportStartDate
	product.TransportEndDate = transportEndDate
	product.InventoryStartDate = inventoryStartDate
	product.Type = productType


	newProductByte, err := json.Marshal(product)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(barcode, newProductByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

func (t *SmartRetailChaincode) addToCart(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error
	fmt.Println("########### example_cc Invoke add to cart ###########with purchaseId"+args[0])
	newCart := Purchase{
		PurchaseId:   args[0],
		ProductId: args[1],
		UserId: args[2],
		ProductName:   args[3],
		Price:     args[4],
		Discount:  args[5],
		Status:    args[6],
		PurchaseDate:   args[7]}

	newCartByte, err := json.Marshal(newCart)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(newCart.PurchaseId, newCartByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	masterByte, err := stub.GetState("MASTER")
	if err != nil {
		return shim.Error(err.Error())
	}

	master := Master{}
	masterByteError := json.Unmarshal(masterByte, &master)
	if masterByteError != nil {
		return shim.Error(err.Error())
	}
	allPurchases := master.AllPurchases
	allPurchases = append(allPurchases, newCart.PurchaseId)
	master.AllPurchases = allPurchases

	newMasterByte, err := json.Marshal(master)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState("MASTER", newMasterByte)
	if err != nil {
		return shim.Error(err.Error())
	}
	
	userByte, err := stub.GetState(newCart.UserId)
	if err != nil {
		return shim.Error(err.Error())
	}
	user := User{}
	userByteErr := json.Unmarshal(userByte, &user)
	if userByteErr != nil {
		return shim.Error(err.Error())
	}
	purchaseId := user.PurchaseId
	purchaseId = append(purchaseId, newCart.PurchaseId)
	user.PurchaseId = purchaseId

	newUserByte, err := json.Marshal(user)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(newCart.UserId, newUserByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

func (t *SmartRetailChaincode) removeFromCart(stub shim.ChaincodeStubInterface, args []string) pb.Response {

var err error
	fmt.Println("########### example_cc Invoke remove from cart ###########with purchaseId"+args[0])


	purchaseByte, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	purchase := Purchase{}
	errUnmarshal := json.Unmarshal(purchaseByte, &purchase)
	if errUnmarshal != nil {
		return shim.Error(errUnmarshal.Error())
	}
		purchaseId :=  args[0]
		productId := args[1]
		userId := args[2]
		productName :=   args[3]
		price :=     args[4]
		discount :=  args[5]
		status :=    args[6]
		purchaseDate := args[7]


	purchase.PurchaseId = purchaseId
    purchase.ProductId = productId
	purchase.UserId = userId
	purchase.ProductName = productName
	purchase.Price = price
	purchase.Discount = discount
	purchase.Status =status
	purchase.PurchaseDate = purchaseDate

	newPurchaseByte, err := json.Marshal(purchase)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(purchaseId, newPurchaseByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)

}
func (t *SmartRetailChaincode) getProductsInCartByUserId(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("###########getProductsInCartByUserId###########"+args[0])
	var err error
	PurchaseByte, err := stub.GetState(args[0])
	fmt.Println(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}
	product := User{}
	PurchaseByteError := json.Unmarshal(PurchaseByte, &product)
	// fmt.Println(product)
	if PurchaseByteError != nil {
		return shim.Error(err.Error())
	}
	fmt.Println(product)
	var allPurchaseId []string
	allPurchaseId = product.PurchaseId
	fmt.Println(product.PurchaseId)

	size := len(allPurchaseId)
	fmt.Println(size)
	var allUserPurchasesInCart []Purchase
	var i int
	for i = 0; i < size; i++ {
		// fmt.Println(allPurchaseId[i])
		purchaseByte, err := stub.GetState(allPurchaseId[i])
		if err != nil {
			return shim.Error(err.Error())
		}
		productsInCart := Purchase{}
		purchaseErr := json.Unmarshal(purchaseByte, &productsInCart)
		if purchaseErr != nil {
			return shim.Error(err.Error())
		}
		// fmt.Println(productsInCart)
			if productsInCart.Status == CART{
		allUserPurchasesInCart = append(allUserPurchasesInCart, productsInCart)

	}
		
	}

	allUserPurchasesInCartByte, err := json.Marshal(allUserPurchasesInCart)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(allUserPurchasesInCartByte)

}

func (t *SmartRetailChaincode) getPurchasesByPurchaseId(stub shim.ChaincodeStubInterface, args []string) pb.Response {

 fmt.Println("###########getPurchasebyPurchaseId###########"+args[0])
	var err error

	purchaseBytes, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(purchaseBytes)

}

func (t *SmartRetailChaincode) makePurchase(stub shim.ChaincodeStubInterface, args []string) pb.Response {

var err error
	fmt.Println("########### makepurchase ###########with purchaseId"+args[7])


	purchaseByte, err := stub.GetState(args[7])
	if err != nil {
		return shim.Error(err.Error())
	}

	purchase := Purchase{}
	errUnmarshal := json.Unmarshal(purchaseByte, &purchase)
	if errUnmarshal != nil {
		return shim.Error(errUnmarshal.Error())
	}
		purchaseId :=  args[7]
		productId := args[0]
		userId := args[4]
		productName :=   args[1]
		price :=     args[2]
		discount :=  args[3]
		status :=    args[6]
		purchaseDate := args[5]
		


	purchase.PurchaseId = purchaseId
    purchase.ProductId= productId
	purchase.UserId=userId
	purchase.ProductName=productName
	purchase.Price=price
	purchase.Discount=discount
	purchase.Status=status
	purchase.PurchaseDate=purchaseDate
	

	newPurchaseByte, err := json.Marshal(purchase)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(purchaseId, newPurchaseByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)

	}
	
	
	func (t *SmartRetailChaincode) getPurchasesByuserId(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("###########getPurchasesByuserId###########")
	var err error
	PurchaseByte, err := stub.GetState(args[0])
	fmt.Println(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}
	product := User{}
	PurchaseByteError := json.Unmarshal(PurchaseByte, &product)
	// fmt.Println(product)
	if PurchaseByteError != nil {
		return shim.Error(err.Error())
	}
	fmt.Println(product)
	var allPurchaseId []string
	allPurchaseId = product.PurchaseId
	fmt.Println(product.PurchaseId)

	size := len(allPurchaseId)
	fmt.Println(size)
	var allUserPurchasesInCart []Purchase
	var i int
	for i = 0; i < size; i++ {
		// fmt.Println(allPurchaseId[i])
		purchaseByte, err := stub.GetState(allPurchaseId[i])
		if err != nil {
			return shim.Error(err.Error())
		}
		productsInCart := Purchase{}
		purchaseErr := json.Unmarshal(purchaseByte, &productsInCart)
		if purchaseErr != nil {
			return shim.Error(err.Error())
		}
		// fmt.Println(productsInCart)
			if productsInCart.Status == COMPLETE{
		allUserPurchasesInCart = append(allUserPurchasesInCart, productsInCart)

	}
		
	}

	allUserPurchasesInCartByte, err := json.Marshal(allUserPurchasesInCart)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(allUserPurchasesInCartByte)
	}
	
	
	func (t *SmartRetailChaincode) getSimilarProducts(stub shim.ChaincodeStubInterface, args []string) pb.Response {

		fmt.Println("###########getSimilarProducts###########"+args[0])
	var err error
	ProductByte, err := stub.GetState(args[0])
	fmt.Println(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}
	product := PackedProduct{}
	ProductByteError := json.Unmarshal(ProductByte, &product)
	// fmt.Println(product)
	if ProductByteError != nil {
		return shim.Error(err.Error())
	}
	fmt.Println(product)
	productName := product.ProductName;
	
	masterByte, err := stub.GetState("MASTER")
	if err != nil {
		return shim.Error(err.Error())
	}
	master := Master{}
		masterByteError := json.Unmarshal(masterByte, &master)
	if masterByteError != nil {
		return shim.Error(err.Error())
	}
	allProducts := master.AllPackedProducts
	

	size := len(allProducts)
	fmt.Println(size)
	var allSimilarProducts []PackedProduct
	var i int
	for i = 0; i < size; i++ {
		
		productsByte, err := stub.GetState(allProducts[i])
		if err != nil {
			return shim.Error(err.Error())
		}
		similarProducts := PackedProduct{}
		productErr := json.Unmarshal(productsByte, &similarProducts)
		if productErr != nil {
			return shim.Error(err.Error())
		}
		
			if similarProducts.ProductName == productName{
		allSimilarProducts = append(allSimilarProducts, similarProducts)

	}
		
	}

	allSimilarProductsByte, err := json.Marshal(allSimilarProducts)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(allSimilarProductsByte)
}








