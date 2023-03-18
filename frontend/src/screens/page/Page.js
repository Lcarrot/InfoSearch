import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";

function Page() {

    const initContent = {text: '', origin: ''}

    const {id} = useParams()
    const [content, setContent] = useState(initContent)

    useEffect(() => {
        fetch('http://localhost:8080/page/' + id)
            .then((response) => response.json())
            .then(respData => setContent(respData))
    }, [id])

    return (
        <div>
            <div className='topic'>
                <Link to='/'><h1> На главную </h1></Link>
                <h1> Текст статьи </h1>
                <a href={content.origin}> Оригинальная статья </a>
                <p> {content.text} </p>
            </div>
        </div>)
}

export default Page;