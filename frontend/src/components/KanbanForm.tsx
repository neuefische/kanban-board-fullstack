import './KanbanForm.css'
import {Dispatch, FormEvent, SetStateAction, useEffect, useState} from "react";
import {getAllItems, importCsv, postNewItem} from "../service/apiService";
import {KanbanItem} from "../service/models";

interface KanbanFormProps{
    onChange: Dispatch<SetStateAction<KanbanItem[]>>
}

export default function KanbanForm(props : KanbanFormProps){

    const [task, setTask] = useState('')
    const [description, setDescription] = useState('')
    const [errorMessage, setErrorMessage] = useState('')

    useEffect(() => {
        const timeoutId = setTimeout(() => setErrorMessage(''), 5000)
        return () => clearTimeout(timeoutId)
    }, [errorMessage])

    const handleSubmit = (ev : FormEvent)=>{
        ev.preventDefault();
        postNewItem({'task':task,'description':description,'status':"OPEN"})
            .then(()=>{
                getAllItems()
                    .then(data =>props.onChange(data))

                setTask("")
                setDescription("")
            })
            .catch(() => setErrorMessage("Task could not be created"))
    }

    const performFileUpload = (file: File) => {
        const fileData = new FormData()
        fileData.append('csv', file)
        importCsv(fileData)
            .then(()=>{
                getAllItems()
                    .then(data =>props.onChange(data))
            })
            .catch(() => setErrorMessage("CSV could not be imported"))
    }

    return(
        <div className="form">
            <form onSubmit={event => handleSubmit(event)}>
                <input type="text" placeholder={'Neue Task'} value={task} onChange={ev=> setTask(ev.target.value)}/>
                <input type="text" placeholder={'Beschreibung'} value={description} onChange={ev=> setDescription(ev.target.value)}/>
                <button type={'submit'}>Hinzufügen</button>
            </form>
            <div>
                <label htmlFor="fileSelection" className="import-label">CSV import</label> <input id="fileSelection" type="file" onChange={ev => performFileUpload(ev.target.files![0])} />
            </div>
            {errorMessage && <div className="error">{errorMessage}</div>}
        </div>
    )
}