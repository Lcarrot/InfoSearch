import {BrowserRouter, Route, Routes} from "react-router-dom";
import Index from "../../screens/index/Index";
import Page from "../../screens/page/Page";

function PageRouter() {
    return <BrowserRouter>
        <Routes>
            <Route element={<Index/>} path='/'/>
            <Route element={<Page/>} path={'/page/:id'} />
            <Route element= {<div> not found </div>} path='*' />
        </Routes>
    </BrowserRouter>
}

export default PageRouter;