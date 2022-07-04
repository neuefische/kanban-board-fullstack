import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"

export function useTimer() {
    const [timeLeft, setTimeLeft] = useState<number>()
    const nav = useNavigate()

    useEffect(() => {
        const timeOutId = setTimeout(() => {
            const jwt = localStorage.getItem('jwt')
            const decoded = window.atob(jwt!.split('.')[1])
            const decodedAsObject = JSON.parse(decoded)
            setTimeLeft(decodedAsObject.exp - (Math.floor(Date.now() / 1000)))
        }, 1000)

        return () => clearTimeout(timeOutId)
    })

    useEffect(() => {
        if (timeLeft && timeLeft < 0) {
            localStorage.removeItem('jwt')
            nav('/')
        }
    }, [timeLeft, nav])

    return timeLeft
}