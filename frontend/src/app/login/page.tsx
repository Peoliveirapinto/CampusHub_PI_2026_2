"use client";

import { useState } from "react";
import Link from "next/link";
import { User } from "lucide-react";

export default function LoginPage() {
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Tentativa de login:", { identifier, password });
    // Aqui conectaremos com a rota da equipe de Java futuramente
  };

  return (
    <div className="min-h-screen bg-white text-gray-900 flex flex-col justify-between">
      {/* Cabeçalho */}
      <header className="w-full flex justify-between items-center px-8 py-6">
        <Link href="/" className="flex items-center gap-3">
          <div className="bg-[#1e2b58] text-white font-bold px-2.5 py-1 rounded-md text-sm tracking-wide">
            CH
          </div>
          <span className="font-bold text-gray-900 text-lg">CampusHub</span>
        </Link>
        <span className="text-gray-900 text-sm font-medium">
          Instituto Mauá de Tecnologia
        </span>
      </header>

      {/* Conteúdo Central / Formulário */}
      <main className="flex-1 flex flex-col items-center justify-center px-4 -mt-12">
        <div className="w-full max-w-sm flex flex-col items-center">
          {/* Ícone de Usuário */}
          <div className="w-20 h-20 rounded-full bg-[#4285f4] flex items-center justify-center text-white mb-4 shadow-sm">
            <User size={48} strokeWidth={2.5} />
          </div>

          {/* Título de Boas-Vindas */}
          <h1 className="text-2xl font-bold text-gray-900 mb-8 tracking-tight text-center">
            Bem Vindo ao CampusHub!
          </h1>

          {/* Formulário */}
          <form onSubmit={handleSubmit} className="w-full flex flex-col gap-4">
            {/* Campo E-mail / RA */}
            <div className="flex flex-col gap-1.5">
              <label 
                htmlFor="identifier" 
                className="text-base font-semibold text-gray-900"
              >
                Email Institucional ou RA
              </label>
              <input
                id="identifier"
                type="text"
                value={identifier}
                onChange={(e) => setIdentifier(e.target.value)}
                placeholder="exemplo@maua.br"
                className="w-full bg-[#b0b0b0] text-gray-800 placeholder-gray-600 px-4 py-2.5 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* Campo Senha */}
            <div className="flex flex-col gap-1.5">
              <label 
                htmlFor="password" 
                className="text-base font-semibold text-gray-900"
              >
                Senha
              </label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Digite sua senha"
                className="w-full bg-[#b0b0b0] text-gray-800 placeholder-gray-600 px-4 py-2.5 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* Esqueci a Senha */}
            <div className="flex justify-start">
              <Link
                href="/recuperar-senha"
                className="text-sm font-medium text-[#4a80e2] hover:underline"
              >
                Esqueceu a senha?
              </Link>
            </div>

            {/* Botão de Login */}
            <button
              type="submit"
              className="w-full mt-2 bg-[#2f6ff7] hover:bg-[#255cd4] text-white font-semibold py-3 rounded-xl transition duration-150 shadow-sm text-lg"
            >
              Login
            </button>
          </form>

          {/* Divisor "ou" */}
          <div className="w-full flex items-center my-5">
            <div className="flex-1 border-t border-gray-400"></div>
            <span className="px-3 text-sm text-gray-700 font-medium">ou</span>
            <div className="flex-1 border-t border-gray-400"></div>
          </div>

          {/* Link para Criar Conta */}
          <Link
            href="/cadastro"
            className="text-base font-bold text-[#0c1b40] hover:underline"
          >
            Criar Conta
          </Link>
        </div>
      </main>

      {/* Espaçador inferior para manter o centro balanceado */}
      <footer className="h-6"></footer>
    </div>
  );
}