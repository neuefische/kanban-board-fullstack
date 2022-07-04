import Header from "../components/Header";
import KanbanGallery from "../components/KanbanGallery";
import {useEffect, useState} from "react";
import KanbanForm from "../components/KanbanForm";
import {KanbanItem} from "../service/models";
import {getAllItems} from "../service/apiService";
import { useNavigate } from "react-router-dom";

export default function KanbanPage(){

    const [items, setItems] = useState([] as Array<KanbanItem>);

    const nav = useNavigate()

    useEffect(()=>{
        getAllItems()
            .then(data => setItems(data))
            .catch(() => nav('/'))            
    }, [nav])


    return(
        <div className={'kanbanPage'}>
            <Header/>
            <KanbanForm onChange={setItems}/>
            <KanbanGallery items={items} onChange={setItems}/>
        </div>
    )
}