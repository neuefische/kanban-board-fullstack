import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {changeItem, getAllItems, getItemById} from "../service/apiService";
import {KanbanItem} from "../service/models";
import Header from "../components/Header";
import './EditPage.css'

export default function EditPage(){
    const {id} = useParams();
    const [item, setItem] = useState({} as KanbanItem)
    const [task, setTask] = useState("")
    const [desc, setDesc] = useState("")


    const nav = useNavigate()

    useEffect(()=>{
        if (id){
        getItemById(id)
            .then(data => {
                setItem(data)
                setTask(data.task)
                setDesc(data.description)
            })
        }
    }, [id])

    const saveItem = () => {
        updateItem()
    }

    const updateItem = () => {
        changeItem({
            'id':item.id,
            'task':task,
            'description':desc,
            'status':item.status
        })
            .then(() => nav('/'))
            .catch(err => console.log(err.message))
    }

    return(
        <div className={'editPage'}>
            <img src="" alt=""/>
            <Header/>
            <div className={'editForm'}>
                <input type="text" placeholder={'Aufgabe'} value={task} onChange={ev=> setTask(ev.target.value)}/>
                <input type="text" placeholder={'Beschreibung'} value={desc} onChange={ev=> setDesc(ev.target.value)}/>
                <button onClick={saveItem}>save</button>
            </div>
        </div>
    )
}