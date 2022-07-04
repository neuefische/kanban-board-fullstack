import axios from "axios";
import {KanbanItem, UserCreationData} from "./models";

export const registerUser = (userCreationData: UserCreationData) => {
    return axios.post('/api/users', userCreationData)
}

export const getAllItems = () =>{
    return axios.get(`api/kanban`)
        .then(response => response.data)
}

export const postNewItem = (item : KanbanItem) =>{
    return axios.post(`api/kanban`,item)
        .then(response => response.data)
}

export const deleteItem = (id ?: string) => {
    return axios.delete(`api/kanban/${id}`)
        .then(response => response.data)
}

export const changeItem = (item : KanbanItem) => {
    return axios.put(`api/kanban`,item)
        .then(response => response.data)
}

export const getItemById = (id : string) => {
    return axios.get(`api/kanban/${id}`)
        .then(response => response.data)
}

export const advanceKanban = (item : KanbanItem) =>{
    return axios.put(`api/kanban/next`,item)
        .then(respone => respone.data)
}

export const returnKanban = (item : KanbanItem) =>{
    return axios.put(`api/kanban/prev`,item)
        .then(respone => respone.data)
}