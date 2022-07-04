import { useTimer } from '../hooks/custom-hooks'
import './Header.css'

export default function Header(){

    const secondsLeft = useTimer()

    return(
        <div className={'header'}>
            <div className={'headline'}><h1>SUPER KANBAN BOARD 9000</h1></div>
            <div className={'timer'}>In {secondsLeft} seconds you need to login again.</div>
        </div>

    )
}