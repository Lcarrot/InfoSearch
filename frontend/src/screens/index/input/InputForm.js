import {useState} from "react";

function InputForm({setPage}) {


    const initData = {request: ''}

    const [data, setData] = useState(initData)

    const find = (e) => {
        e.preventDefault()
        fetch('http://localhost:8080/search/' + data.request)
            .then((response) => response.json())
            .then(respData => setPage(respData))
        setData(initData)
    }

    return (<form>
        <input placeholder='Введите запрос'
               onChange={e => setData(prev => ({...prev, request: e.target.value}))}
               value={data.request}/>
        <button className='button' onClick={(e) => find(e)}>Найти</button>
    </form>)
}

export default InputForm;