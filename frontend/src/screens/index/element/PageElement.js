import {Link} from "react-router-dom";

function PageElement({path, tfIdf}) {

    const pagePath = path.replaceAll('.txt', '')
    return (
        <div>
            <p>найден файл: {pagePath} с tfIdf = {tfIdf}</p>
            <Link to={'/page/' + pagePath}> посмотреть файл </Link>
        </div>)
}

export default PageElement;