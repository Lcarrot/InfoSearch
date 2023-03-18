function PageElement({path, tfIdf}) {

    const pagePath = path.replaceAll('.txt', '')
    return (
        <div>
            <p>найден файл: {pagePath} с tfIdf = {tfIdf}</p>
            <a href={'/page/' + pagePath}> посмотреть файл </a>
        </div>)
}

export default PageElement;