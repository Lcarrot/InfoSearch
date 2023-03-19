import {useState} from "react";
import styles from "./InputForm.module.scss";

function InputForm({setPage}) {


    const initData = {request: ''}

    const [data, setData] = useState(initData)

    const find = (e) => {
        e.preventDefault()
        fetch('http://localhost:8080/search/' + data.request)
            .then((response) => response.json())
            .then(respData => setPage(respData == null ? [] : respData))
        setData(initData)
    }

    return (
        <div className='formContainer'>
            <form className={styles.form}>
                <input placeholder='Введите запрос' className={styles.input}
                       onChange={e => setData(prev => ({...prev, request: e.target.value}))}
                       value={data.request}/>
                <button className={styles.button} onClick={(e) => find(e)}>Найти</button>
            </form>
        </div>
    )
}

export default InputForm;