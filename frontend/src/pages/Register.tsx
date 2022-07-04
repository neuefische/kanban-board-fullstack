import { FormEvent, useState } from "react"
import { useNavigate } from "react-router-dom"
import { registerUser } from "../service/apiService"

export default function Register() {

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [passwordRepeat, setPasswordRepeat] = useState('')
    const [errorMessage, setErrorMessage] = useState('')

    const nav = useNavigate()

    const register = (ev: FormEvent) => {
        ev.preventDefault()
        registerUser({ username, password, passwordRepeat })
            .then(() => nav('/'))
            .catch(() => setErrorMessage('User could not be created.'))
    }

    return (
        <div>
            <h3>Get an account</h3>
            <form onSubmit={register}>
                <input type="text" value={username} onChange={ev => setUsername(ev.target.value)} placeholder="Username" />
                <input type="password" value={password} onChange={ev => setPassword(ev.target.value)} placeholder="Password" />
                <input type="password" value={passwordRepeat} onChange={ev => setPasswordRepeat(ev.target.value)} placeholder="Repeat the password" />
                <input type="submit" value="Register" />
                { errorMessage && <div>{errorMessage}</div> }
            </form>
        </div>
    )
}