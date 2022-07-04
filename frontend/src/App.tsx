import React from 'react';
import KanbanPage from "./pages/KanbanPage";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import EditPage from "./pages/EditPage";
import Register from './pages/Register';
import Login from './pages/Login';

function App() {
  return (
      <BrowserRouter>
          <Routes>
              <Route path={'/'} element={<Login />} />
              <Route path={'/register'} element={<Register />} />
              <Route path={'/app'} element={<KanbanPage/>}/>
              <Route path={'/:id'} element={<EditPage/>}/>
          </Routes>
      </BrowserRouter>
  );
}

export default App;
