import Header from "../components/Header";
import KanbanGallery from "../components/KanbanGallery";
import React, {useEffect, useState} from "react";
import KanbanForm from "../components/KanbanForm";
import {KanbanItem} from "../service/models";
import {getAllItems} from "../service/apiService";

export default function KanbanPage(){

    const [items, setItems] = useState([] as Array<KanbanItem>);


    useEffect(()=>{
        getAllItems()
            .then(data => setItems(data))
    },[])


    return(
        <div className={'kanbanPage'}>
            <Header/>
            <KanbanForm onChange={setItems}/>
            <KanbanGallery items={items} onChange={setItems}/>
        </div>
    )
}