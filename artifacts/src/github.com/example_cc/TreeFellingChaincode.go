package main

import (
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)
type TreeFellingChaincode struct {
}

// Master to store required details of all entities in a blockchain
type Master struct {
	AllUsers            []string
	AllLands    []string
	AllTrees         []string
	AllPermits []string	
}

// User detailed structure
type User struct {
	Email    string
	UserName  string
	MobileNo  string
	UserType  string
    AadharNo  string
	Address   string
	Password  string
	LandId    []string
	PermitId  []string
}


// Land detailed structure
type Land struct {
	LandId                 string
	UserId                 string
	District               string
	Taluk                  string
	GPS                    string
	TotalExtent            string
	TreeId                []string

}

// Tree detailed structure
type Tree struct {
	TreeId                 string
	Location               string
	SurveyNo               string
	ApproxTreeArea         string
	PermitId               string
	YearOfPlantation       string
	Status                 string
	LandId                 string

}

// Permit detailed structure
type Permit struct {
	PermitId             string
	UserId               string
	Status               string
	TreeId               []string
    LandId               string
	SurveyDocHash        string
	LandDocHash          string
	RevenueDocHash       string
	PoliceDocHash        string
	RTODocHash 			 string
	ForestDocHash		 string
	SawMillDocHash       string
	BescomDocHash        string
}

func main() {
	err := shim.Start(new(TreeFellingChaincode))
	if err != nil {

	}
}

func (t *TreeFellingChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {

	fmt.Println("########### TreeFellingChaincode Init ###########")
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
func (t *TreeFellingChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()

	if function == "addUser" {
		return t.addUser(stub, args)
	}

	if function == "getUserById" {
		return t.getUserById(stub, args)
	}
	if function == "getLandsByUserId" {
		return t.getLandsByUserId(stub, args)
	}
	
	if function == "getTreesByLandId" {
		return t.getTreesByLandId(stub, args)
	}
	if function == "registerLand" {
		return t.registerLand(stub, args)
	}
	if function == "addTree" {
		return t.addTree(stub, args)
	}
	
	
	return shim.Success(nil)
}

func (t *TreeFellingChaincode) addUser(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error
	fmt.Println("########### TreeFellingChaincode Invoke add user")
	newUser := User{
		Email:   args[0],
		UserName: args[1],
		MobileNo:   args[2],
		UserType:     args[3],
		AadharNo:   args[4],
		Address:   args[5],
		Password:     args[6]}

	newUserByte, err := json.Marshal(newUser)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(newUser.Email, newUserByte)
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
	allUsers = append(allUsers, newUser.Email)
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

func (t *TreeFellingChaincode) getUserById(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error
	fmt.Println("########### TreeFellingChaincode Query get user by ID")
	UserBytes, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(UserBytes)

}
func (t *TreeFellingChaincode) registerLand(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error
	fmt.Println("########### TreeFellingChaincode Invoke register land")
	newLand := Land{
		LandId:   args[0],
		UserId: args[1],
		District:   args[2],
		Taluk:     args[3],
		GPS:   args[4],
		TotalExtent:   args[5]}

	newLandByte, err := json.Marshal(newLand)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(newLand.LandId, newLandByte)
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
	allLands := master.AllLands
	allLands = append(allLands, newLand.LandId)
	master.AllLands = allLands

	newMasterByte, err := json.Marshal(master)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState("MASTER", newMasterByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	userByte, err := stub.GetState(newLand.UserId)
	if err != nil {
		return shim.Error(err.Error())
	}
	user := User{}
	userByteErr := json.Unmarshal(userByte, &user)
	if userByteErr != nil {
		return shim.Error(err.Error())
	}
	landId := user.LandId
	landId = append(landId, newLand.LandId)
	user.LandId = landId

	newUserByte, err := json.Marshal(user)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(newLand.UserId, newUserByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}


func (t *TreeFellingChaincode) getLandsByUserId(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("###########getLandsByUserId###########")
	var err error
	UserByte, err := stub.GetState(args[0])
	fmt.Println(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}
	user := User{}
	UserByteError := json.Unmarshal(UserByte, &user)
	if UserByteError != nil {
		return shim.Error(err.Error())
	}
	fmt.Println(user)
	var allLandId []string
	allLandId = user.LandId
	size := len(allLandId)
	fmt.Println(size)
	var allLands []Land
	var i int
	for i = 0; i < size; i++ {
		landByte, err := stub.GetState(allLandId[i])
		if err != nil {
			return shim.Error(err.Error())
		}
		landOwnedByUser := Land{}
		landErr := json.Unmarshal(landByte, &landOwnedByUser)
		if landErr != nil {
			return shim.Error(err.Error())
		}

		allLands = append(allLands, landOwnedByUser)
		
	}

	allLandsByUserByte, err := json.Marshal(allLands)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(allLandsByUserByte)

}

func (t *TreeFellingChaincode) addTree(stub shim.ChaincodeStubInterface, args []string) pb.Response {

	var err error
	fmt.Println("########### TreeFellingChaincode Invoke add Tree")
	newTree := Tree{
		TreeId:   args[0],
		Location: args[1],
		SurveyNo:   args[2],
		ApproxTreeArea:     args[3],
		PermitId:   args[4],
		YearOfPlantation:   args[5],
		Status:     args[6],
		LandId:     args[7]}

	newTreeByte, err := json.Marshal(newTree)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(newTree.TreeId, newTreeByte)
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
	allTrees := master.AllTrees
	allTrees = append(allTrees, newTree.TreeId)
	master.AllTrees = allTrees

	newMasterByte, err := json.Marshal(master)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState("MASTER", newMasterByte)
	if err != nil {
		return shim.Error(err.Error())
	}
	
	landByte, err := stub.GetState(newTree.LandId)
	if err != nil {
		return shim.Error(err.Error())
	}
	land := Land{}
	landByteErr := json.Unmarshal(landByte, &land)
	if landByteErr != nil {
		return shim.Error(err.Error())
	}
	treeId := land.TreeId
	treeId = append(treeId, newTree.TreeId)
	land.TreeId = treeId

	newLandByte, err := json.Marshal(land)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(newTree.LandId, newLandByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}
func (t *TreeFellingChaincode) getTreesByLandId(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("###########getLandsByUserId###########")
	var err error
	LandByte, err := stub.GetState(args[0])
	fmt.Println(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}
	land := Land{}
	LandByteError := json.Unmarshal(LandByte, &land)
	if LandByteError != nil {
		return shim.Error(err.Error())
	}
	fmt.Println(land)
	var allTreeId []string
	allTreeId = land.TreeId
	size := len(allTreeId)
	fmt.Println(size)
	var allTrees []Tree
	var i int
	for i = 0; i < size; i++ {
		treeByte, err := stub.GetState(allTreeId[i])
		if err != nil {
			return shim.Error(err.Error())
		}
		treesPerLand := Tree{}
		treeErr := json.Unmarshal(treeByte, &treesPerLand)
		if treeErr != nil {
			return shim.Error(err.Error())
		}

		allTrees = append(allTrees, treesPerLand)
		
	}

	allTreesByLandByte, err := json.Marshal(allTrees)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(allTreesByLandByte)

}


	
	

	
	







