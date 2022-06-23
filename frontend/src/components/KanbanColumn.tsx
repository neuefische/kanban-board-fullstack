import KanbanCard from "./KanbanCard";
import './KanbanColumn.css'
import {KanbanItem} from "../service/models";
import {Dispatch, SetStateAction} from "react";

interface KanbanColumnInterface{
    tasks : Array<KanbanItem>
    onChange : Dispatch<SetStateAction<KanbanItem[]>>
}

export default function KanbanColumn(props : KanbanColumnInterface){
    return(
        <div className={'column'}>
            {props.tasks.map(item =>
                <KanbanCard infos={item} onChange={props.onChange}/>
            )}
        </div>
    )
}