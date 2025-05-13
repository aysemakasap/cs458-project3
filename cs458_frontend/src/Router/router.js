import {createBrowserRouter, RouterProvider} from 'react-router-dom';
import Login from '../pages/login';
import AfterPage from '../pages/afterPage';
import SurveyPage from '../pages/SurveyPage';
import CreateCustomSurvey from '../pages/CreateCustomSurveyPage';
import FillSurvey from '../pages/fillSurvey';
import GoToCustomSurvey from '../pages/goToCustomSurvey';

const Router = () => {
    return <RouterProvider router={router}/>;
};

const router = createBrowserRouter([
    {path: '', element: <Login/>},
    {path: '/success', element: <AfterPage/>},
    {path: '/survey', element: <SurveyPage/>},
    {path: '/createCustomSurvey', element: <CreateCustomSurvey/>},
    { path: '/fillSurvey/:id', element: <FillSurvey/> },
    { path: '/goToCustomSurvey', element: <GoToCustomSurvey/> }
]);

export default Router;