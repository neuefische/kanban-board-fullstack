export const status = ["Open","In Progress","Done"]
export const statusEnum = ["OPEN","IN_PROGRESS","DONE"]

export interface KanbanItem {
    id ?: string,
    task : string,
    description : string,
    status : string;
}

export interface UserCreationData {
    username: string
    password: string
    passwordRepeat: string
}