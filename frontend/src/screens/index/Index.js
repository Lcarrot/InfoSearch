import InputForm from "./input/InputForm";
import {useState} from "react";
import PageElement from "./element/PageElement";

function Index() {

    const [pages, setPage] = useState([])

    return (
        <div>
            <h1>Поиск страниц</h1>
            <InputForm setPage={setPage}/>
            <div className='topic'>
                По вашему запросу найдено {pages.length} результатов:
                {pages.map(page => <PageElement key={page.fileName} path={page.fileName} tfIdf = {page.tfIdf}/>)}
            </div>
        </div>)
}

export default Index;