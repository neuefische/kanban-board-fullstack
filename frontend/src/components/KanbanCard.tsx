import './KanbanCard.css'
import {KanbanItem} from "../service/models";
import {advanceKanban, deleteItem, getAllItems, returnKanban} from "../service/apiService";
import {Dispatch, SetStateAction} from "react";
import {useNavigate} from "react-router-dom";

interface KanbanCardProps{
    infos : KanbanItem
    onChange : Dispatch<SetStateAction<KanbanItem[]>>
}

export default function KanbanCard({infos, onChange} : KanbanCardProps){

    const nav = useNavigate()

    const removeItem = () =>{
        deleteItem(infos.id)
            .then(() => {
                getAllItems()
                    .then(data => onChange(data))
            })
            .catch(err => console.log(err))
    }

    const advanceItem = () => {
        advanceKanban(infos)
            .then(() => refresh())
    }

    const returnItem = () => {
        returnKanban(infos)
            .then(() => refresh())
    }
    

    const refresh = () => {
        getAllItems()
            .then(data => onChange(data))
            .catch(err => console.log(err.message))
    }


        return(<div className={'card'}>
                <h3>{infos.task}</h3>
                <p>{infos.description}</p>
                <div className={'cardBtn'}>
                    {infos.status==="OPEN"?
                        <button className={'btn'} onClick={()=>removeItem()}>{"<Delete"}</button>
                        :
                        <button className={'btn'} onClick={()=>returnItem()}>{"<Back"}</button>
                    }
                    <button className={'btn'} onClick={()=>nav('/'+infos.id)}>Edit</button>
                    {infos.status==="DONE"?
                        <button className={'btn'} onClick={()=>removeItem()}>{"Delete>"}</button>
                        :
                        <button className={'btn'} onClick={()=>advanceItem()}>{"Next>"}</button>
                    }
                </div>
            </div>)


}