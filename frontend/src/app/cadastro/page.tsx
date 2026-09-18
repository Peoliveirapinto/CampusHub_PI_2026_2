"use client";

import { useState } from "react";
import Link from "next/link";
import { ChevronDown } from "lucide-react";

export default function CadastroPage() {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [curso, setCurso] = useState("");
  const [senha, setSenha] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Dados do cadastro:", { nome, email, curso, senha });
    // Futura integração com o endpoint Java (ex: POST /api/auth/register)
  };

  return (
    <div className="min-h-screen bg-[#f1f5f9] text-gray-900 flex flex-col justify-between">
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

      {/* Conteúdo Central / Card de Cadastro */}
      <main className="flex-1 flex items-center justify-center px-4 py-8">
        <div className="w-full max-w-md bg-white border border-gray-400/80 rounded-2xl p-8 shadow-sm">
          
          {/* Topo do Card */}
          <div className="flex justify-between items-start mb-2">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Criar conta</h1>
              <p className="text-xs text-gray-600 mt-0.5 italic">
                Faça parte da rede acadêmica da Mauá
              </p>
            </div>
            <span className="bg-[#cbe8fd] text-[#0284c7] text-xs font-semibold px-3 py-1 rounded-full">
              Passo 1 de 2
            </span>
          </div>

          <div className="border-t border-gray-300 my-4"></div>

          {/* Formulário */}
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            {/* Nome Completo */}
            <div className="flex flex-col gap-1">
              <label 
                htmlFor="nome" 
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                NOME COMPLETO
              </label>
              <input
                id="nome"
                type="text"
                placeholder="Ex: Mario Bros"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* E-mail Institucional */}
            <div className="flex flex-col gap-1">
              <label 
                htmlFor="email" 
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                E-MAIL INSTITUCIONAL
              </label>
              <input
                id="email"
                type="email"
                placeholder="Ex: 21.1111-1@maua.br"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* Curso */}
            <div className="flex flex-col gap-1">
              <label 
                htmlFor="curso" 
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                CURSO
              </label>
              <div className="relative">
                <select
                  id="curso"
                  value={curso}
                  onChange={(e) => setCurso(e.target.value)}
                  className="w-full bg-[#dedede] text-gray-800 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm appearance-none cursor-pointer"
                  required
                >
                  <option value="" disabled>Selecione seu curso</option>
                  <option value="ciencia-da-computacao">Ciência da Computação</option>
                  <option value="sistemas-de-informacao">Sistemas de Informação</option>
                  <option value="engenharia-de-computacao">Engenharia de Computação</option>
                  <option value="engenharia-mecanica">Engenharia Mecânica</option>
                  <option value="engenharia-eletrica">Engenharia Elétrica</option>
                  <option value="engenharia-civil">Engenharia Civil</option>
                  <option value="design">Design</option>
                  <option value="administracao">Administração</option>
                </select>
                <ChevronDown 
                  size={18} 
                  className="absolute right-3.5 top-1/2 -translate-y-1/2 text-gray-700 pointer-events-none" 
                />
              </div>
            </div>

            {/* Senha */}
            <div className="flex flex-col gap-1">
              <label 
                htmlFor="senha" 
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                SENHA
              </label>
              <input
                id="senha"
                type="password"
                placeholder="Minimo de 8 caracteeres"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
                minLength={8}
              />
            </div>

            {/* Botão Cadastrar Conta */}
            <button
              type="submit"
              className="w-full mt-2 bg-[#2f6ff7] hover:bg-[#255cd4] text-white font-semibold py-3 rounded-xl transition duration-150 shadow-sm text-sm"
            >
              Cadastrar Conta
            </button>
          </form>

          {/* Link para Login */}
          <div className="text-center mt-4">
            <span className="text-xs text-gray-700 font-medium">
              Já tem uma conta?{" "}
            </span>
            <Link
              href="/login"
              className="text-xs font-semibold text-[#1e2b58] hover:underline"
            >
              Fazer Login
            </Link>
          </div>

        </div>
      </main>

      {/* Espaçador inferior */}
      <footer className="h-6"></footer>
    </div>
  );
}