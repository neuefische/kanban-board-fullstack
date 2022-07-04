import axios, { AxiosResponse } from "axios";
import {KanbanItem, LoginData, LoginResponse, UserCreationData} from "./models";

export const registerUser = (userCreationData: UserCreationData) => {
    return axios.post('/api/users', userCreationData)
}

export const loginUser = (loginData: LoginData) => {
    return axios.post('/api/auth/login', loginData)
        .then((response: AxiosResponse<LoginResponse>) => response.data)
}

export const getAllItems = () =>{
    return axios.get(`api/kanban`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then(response => {
        refreshToken()
        return response.data
    })
}

export const postNewItem = (item : KanbanItem) =>{
    return axios.post(`api/kanban`, item, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then(response => {
        refreshToken()
        return response.data
    })
}

export const deleteItem = (id ?: string) => {
    return axios.delete(`api/kanban/${id}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then(response => {
        refreshToken()
        return response.data
    })
}

export const changeItem = (item : KanbanItem) => {
    return axios.put(`api/kanban`,item, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then(response => {
        refreshToken()
        return response.data
    })
}

export const getItemById = (id : string) => {
    return axios.get(`api/kanban/${id}`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then(response => {
        refreshToken()
        return response.data
    })
}

export const advanceKanban = (item : KanbanItem) =>{
    return axios.put(`api/kanban/next`,item, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then(response => {
        refreshToken()
        return response.data
    })
}

export const returnKanban = (item : KanbanItem) =>{
    return axios.put(`api/kanban/prev`, item, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then(response => {
        refreshToken()
        return response.data
    })
}

const refreshToken = () => {
    axios.post('/api/auth/refresh', null, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt')}`
        }
    })
    .then((response: AxiosResponse<LoginResponse>) => response.data)
    .then(loginResponse => localStorage.setItem('jwt', loginResponse.jwt))
}