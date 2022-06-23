import KanbanColumn from "./KanbanColumn";
import './KanbanGallery.css'
import {KanbanItem} from "../service/models";
import {status, statusEnum} from "../service/models";
import {Dispatch, SetStateAction} from "react";

interface KanbanGalleryProps{
    items : Array<KanbanItem>
    onChange : Dispatch<SetStateAction<KanbanItem[]>>
}

export default function KanbanGallery(props : KanbanGalleryProps){


    return(
        <div className={'gallery'}>
            {status.map((item, index)=> <div>
                <h1 className={'galleryHeader'}>{item}</h1>
                <KanbanColumn tasks={props.items.filter(task => task.status===statusEnum[index])} onChange={props.onChange}/>
            </div>)}
        </div>
    )
}