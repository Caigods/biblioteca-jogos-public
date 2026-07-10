import { Navigate, Route, Routes } from 'react-router-dom'
import LoginPage from '../pages/LoginPage/LoginPage'
import GamesPage from '../pages/GamesPage/GamesPage'
import GameDetailsPage from '../pages/GameDetailsPage/GameDetailsPage'
import GameFormPage from '../pages/GameFormPage/GameFormPage'

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/jogos" element={<GamesPage />} />
      <Route path="/jogos/novo" element={<GameFormPage />} />
      <Route path="/jogos/:id" element={<GameDetailsPage />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  )
}

export default AppRoutes
