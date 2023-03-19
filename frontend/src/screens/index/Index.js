import InputForm from "./input/InputForm";
import {useState} from "react";
import PageElement from "./element/PageElement";

function Index() {

    const [pages, setPage] = useState(null)

    function getPageCount() {
        return pages.length > 0 ? pages.length : 0;
    }

    return (
        <div>
            <h1>Поиск страниц</h1>
            <InputForm setPage={setPage}/>
            <div className='formContainer'>
                <div className='findPages'>
                    {pages != null &&
                        <p> По вашему запросу найдено {getPageCount()} страниц: </p>
                    }
                </div>
            </div>
            <div className='formContainer'>
                <div className='findPages'>
                    {pages != null && pages.length > 0 &&
                        pages.map(page => <PageElement key={page.fileName} path={page.fileName} tfIdf={page.tfIdf}/>)}
                </div>
            </div>
        </div>)
}

export default Index;