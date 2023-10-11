import {UserType} from "../modules/user";

let currentUser : UserType;


const setCurrentUserState = (user: UserType)=>{
    currentUser = user;
}


const getCurrentUserState = () :UserType=>{
    return currentUser;
}

export {
    getCurrentUserState,
    setCurrentUserState,
}