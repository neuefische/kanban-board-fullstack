import { useTimer } from '../hooks/custom-hooks'
import './Header.css'

export default function Header(){

    const secondsLeft = useTimer()

    const minutes = Math.floor((secondsLeft ?? 0) / 60)
    const seconds = (secondsLeft ?? 0) - minutes * 60;

    return(
        <div className={'header'}>
            <div className={'headline'}><h1>SUPER KANBAN BOARD 9000</h1></div>
            <div className={'timer'}>In {minutes} minutes and {seconds} seconds you need to login again.</div>
        </div>

    )
}