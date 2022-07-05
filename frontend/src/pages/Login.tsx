import { FormEvent, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { loginUser } from "../service/apiService"

export default function Login() {

    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [errorMessage, setErrorMessage] = useState('')

    const nav = useNavigate()

    const login = (ev: FormEvent) => {
        ev.preventDefault()
        loginUser({username, password})
            .then(loginResponse => localStorage.setItem('jwt', loginResponse.jwt))
            .then(() => nav('/app'))
            .catch(() => setErrorMessage('Login failed'))
    }

    return (
        <div>
            <h3>Login</h3>
            You don't have an account? <Link to="/register">Get one, FAST! ASAP!!!</Link>
            <form onSubmit={login}>
                <input type="text" value={username} onChange={ev => setUsername(ev.target.value)} placeholder="Username" />
                <input type="password" value={password} onChange={ev => setPassword(ev.target.value)} placeholder="Password" />
                <input type="submit" value="Login" />
                { errorMessage && <div>{errorMessage}</div> }
            </form>

        </div>
    )
}