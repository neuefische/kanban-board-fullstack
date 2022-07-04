import './KanbanForm.css'
import {Dispatch, FormEvent, SetStateAction, useState} from "react";
import {getAllItems, postNewItem} from "../service/apiService";
import {KanbanItem} from "../service/models";

interface KanbanFormProps{
    onChange: Dispatch<SetStateAction<KanbanItem[]>>
}

export default function KanbanForm(props : KanbanFormProps){

    const [task, setTask] = useState("" );
    const [description, setDescription] = useState("")

    const handleSubmit = (ev : FormEvent)=>{
        ev.preventDefault();
        postNewItem({'task':task,'description':description,'status':"OPEN"})
            .then(()=>{
                getAllItems()
                    .then(data =>props.onChange(data))
            })
            .catch(err=> console.log(err.message))
        setTask("")
        setDescription("")
    }

    return(
        <form className={'form'} onSubmit={event => handleSubmit(event)}>
            <input type="text" placeholder={'Neue Task'} value={task} onChange={ev=> setTask(ev.target.value)}/>
            <input type="text" placeholder={'Beschreibung'} value={description} onChange={ev=> setDescription(ev.target.value)}/>
            <button type={'submit'}>Hinzufügen</button>
        </form>
    )
}