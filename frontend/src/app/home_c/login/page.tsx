"use client";

import React, { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import {
  Search,
  Bell,
  Users,
  Puzzle,
  UserCheck,
  User,
  ArrowRight,
  ExternalLink,
  Plus,
  Compass,
} from "lucide-react";

export default function HubPage() {
  const router = useRouter();
  const [busca, setBusca] = useState<string>("");

  // Atalhos institucionais da Mauá
  const atalhosUniversitarios = [
    { nome: "Eureka", href: "https://eureka.maua.br", icone: "💡" },
    { nome: "Calendário Acadêmico", href: "#", icone: "📅" },
    { nome: "Entidades Acadêmicas", href: "#", icone: "👥" },
    { nome: "Intercâmbio & RI", href: "#", icone: "🌎" },
    { nome: "RU / Cardápio", href: "#", icone: "🍽️" },
  ];

  return (
    <div className="min-h-screen bg-[#F0F4F8] text-slate-800 font-sans flex flex-col">
      {/* 1. NAVBAR SUPERIOR LOGADA */}
      <nav className="sticky top-0 z-40 bg-white border-b border-slate-200 px-4 sm:px-8 h-16 flex items-center justify-between gap-4">
        {/* Lado Esquerdo: Logo PNG */}
        <div className="flex items-center shrink-0">
          <Link
            href="/"
            className="flex items-center transition-opacity hover:opacity-90"
          >
            <Image
              src="/logo_CampusHub_sem_fundo.png"
              alt="CampusHub Logo"
              width={160}
              height={40}
              className="h-8.5 w-auto object-contain"
              priority
            />
          </Link>
        </div>

        {/* Centro: Barra de Pesquisa de Alunos por Habilidade */}
        <div className="flex-1 max-w-xl mx-2 sm:mx-4">
          <div className="relative flex items-center w-full">
            <Search
              size={18}
              className="absolute left-3.5 text-slate-400 pointer-events-none"
            />
            <input
              type="text"
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
              placeholder="Buscar alunos por habilidade ( ex: Python, UX/UI, C++)"
              className="w-full bg-[#E5E7EB]/70 hover:bg-[#E5E7EB] focus:bg-white text-xs sm:text-sm text-slate-800 placeholder-slate-500 rounded-xl pl-10 pr-4 py-2 outline-none border border-transparent focus:border-slate-300 transition"
            />
          </div>
        </div>

        {/* Lado Direito: Notificações + Perfil do Aluno */}
        <div className="flex items-center gap-4 sm:gap-6 shrink-0">
          {/* Sino com Alerta */}
          <button
            type="button"
            className="relative p-1.5 text-slate-600 hover:text-slate-900 transition cursor-pointer"
            title="Notificações"
          >
            <Bell size={20} />
            <span className="absolute top-1 right-1 w-2.5 h-2.5 bg-red-500 rounded-full ring-2 ring-white"></span>
          </button>

          {/* Divisor vertical */}
          <div className="h-7 w-px bg-slate-200 hidden sm:block"></div>

          {/* Informações do Aluno */}
          <Link
            href="/editar_perfil"
            className="flex items-center gap-3 group cursor-pointer"
          >
            <div className="w-10 h-10 rounded-full bg-[#2563EB] text-white flex items-center justify-center shrink-0 group-hover:ring-2 group-hover:ring-blue-400 transition">
              <User size={24} />
            </div>
            <div className="hidden md:flex flex-col text-left">
              <span className="text-sm font-bold text-slate-900 group-hover:text-blue-900 transition">
                Mario Bros
              </span>
              <span className="text-[11px] text-slate-500 font-medium">
                Engenharia - IMT
              </span>
            </div>
          </Link>
        </div>
      </nav>

      {/* 2. CONTEÚDO PRINCIPAL (GRID 3 COLUNAS) */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-start">
          
          {/* =========================================
              COLUNA 1: MENU LATERAL & PERFIL BASE (3 cols)
             ========================================= */}
          <div className="lg:col-span-3 space-y-6">
            {/* Bloco de Navegação */}
            <div className="bg-white rounded-2xl border border-slate-200/80 p-3 shadow-xs">
              <div className="px-3 py-2 text-xs font-bold uppercase tracking-wider text-slate-400">
                Início / Hub
              </div>
              <div className="mt-1 space-y-1">
                <Link
                  href="/meus_grupos"
                  className="flex items-center justify-between px-3 py-2.5 rounded-xl text-xs sm:text-sm font-bold text-slate-800 hover:bg-slate-100 transition"
                >
                  <div className="flex items-center gap-2.5">
                    <Users size={18} className="text-slate-600" />
                    <span>Meus Grupos</span>
                  </div>
                  <span className="w-6 h-6 rounded-full bg-blue-100 text-blue-700 text-xs font-bold flex items-center justify-center">
                    2
                  </span>
                </Link>

                <Link
                  href="/matches"
                  className="flex items-center justify-between px-3 py-2.5 rounded-xl text-xs sm:text-sm font-bold text-slate-800 hover:bg-slate-100 transition"
                >
                  <div className="flex items-center gap-2.5">
                    <Puzzle size={18} className="text-slate-600" />
                    <span>Matches & Vagas</span>
                  </div>
                  <span className="px-2 py-0.5 rounded-md bg-amber-100 text-amber-800 text-[10px] font-extrabold uppercase">
                    Novo
                  </span>
                </Link>

                <Link
                  href="/editar_perfil"
                  className="flex items-center gap-2.5 px-3 py-2.5 rounded-xl text-xs sm:text-sm font-bold text-slate-800 hover:bg-slate-100 transition"
                >
                  <UserCheck size={18} className="text-slate-600" />
                  <span>Editar Perfil</span>
                </Link>
              </div>
            </div>

            {/* Bloco Azul Escuro: SEU PERFIL BASE */}
            <div className="bg-[#0B0D63] text-white rounded-2xl p-5 shadow-sm space-y-3">
              <p className="text-[11px] font-black uppercase tracking-wider text-lime-400">
                Seu Perfil Base
              </p>
              <div>
                <h3 className="font-bold text-base">Mario Bros</h3>
                <p className="text-xs text-blue-200 mt-0.5">
                  Engenharia - 5º Semestre
                </p>
              </div>

              <div className="h-px bg-white/15 my-2"></div>

              <div className="flex flex-wrap gap-1.5 pt-1">
                {["Python", "Docker", "MongoDB"].map((tech) => (
                  <span
                    key={tech}
                    className="px-2.5 py-1 bg-white/20 hover:bg-white/25 rounded-md text-[11px] font-medium transition"
                  >
                    {tech}
                  </span>
                ))}
              </div>
            </div>
          </div>

          {/* =========================================
              COLUNA 2: FEED PRINCIPAL DE PI & VAGAS (6 cols)
             ========================================= */}
          <div className="lg:col-span-6 space-y-6">
            
            {/* Banner: Procurando grupo para PI? */}
            <div className="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-xs flex flex-col md:flex-row md:items-center justify-between gap-4">
              <div className="max-w-xs">
                <h2 className="text-xl font-bold text-slate-900 tracking-tight">
                  Procurando grupo para PI?
                </h2>
                <p className="text-xs text-slate-500 mt-1 leading-relaxed">
                  Crie seu grupo definindo as skills que faltam ou busque matches complementares.
                </p>
              </div>

              <div className="flex flex-wrap sm:flex-nowrap items-center gap-2.5 shrink-0">
                <button
                  type="button"
                  onClick={() => router.push("/criar_grupo")}
                  className="px-4 py-2.5 bg-[#2563EB] hover:bg-blue-700 text-white font-bold text-xs rounded-xl shadow-xs transition cursor-pointer flex items-center gap-1.5"
                >
                  <Plus size={14} /> Criar Grupo
                </button>
                <button
                  type="button"
                  onClick={() => router.push("/matches")}
                  className="px-4 py-2.5 bg-[#52C41A] hover:bg-green-600 text-white font-bold text-xs rounded-xl shadow-xs transition cursor-pointer"
                >
                  Ver Matches
                </button>
              </div>
            </div>

            {/* Seção: Meus Grupos Ativos */}
            <div className="space-y-3">
              <div className="flex justify-between items-center px-1">
                <h3 className="font-bold text-base text-slate-900">
                  Meus Grupos Ativos
                </h3>
                <Link
                  href="/meus_grupos"
                  className="text-xs font-semibold text-blue-600 hover:text-blue-800 transition"
                >
                  Ver todos
                </Link>
              </div>

              {/* Card do Grupo CampusHub */}
              <div className="bg-white rounded-2xl border border-slate-200/80 p-5 shadow-xs relative">
                <div className="flex justify-between items-start">
                  <span className="px-2.5 py-0.5 rounded-md bg-blue-100 text-blue-700 text-[11px] font-bold">
                    Projeto Integrador
                  </span>
                  <span className="text-xs font-bold text-amber-600">
                    5/6 alunos
                  </span>
                </div>

                <h4 className="font-bold text-lg text-slate-900 mt-2">
                  CampusHub
                </h4>
                <p className="text-xs text-slate-500 mt-0.5">
                  Desenvolvimento do protótipo e documentação
                </p>

                <div className="h-px bg-slate-100 my-4"></div>

                <div className="flex items-center justify-between">
                  {/* Avatares dos Integrantes */}
                  <div className="flex items-center -space-x-2">
                    {[1, 2, 3, 4, 5].map((_, index) => (
                      <div
                        key={index}
                        className="w-8 h-8 rounded-full bg-[#1D3557] border-2 border-white text-white flex items-center justify-center text-xs font-bold shadow-2xs"
                      >
                        <User size={16} />
                      </div>
                    ))}
                  </div>

                  <Link
                    href="/grupo/campushub"
                    className="inline-flex items-center gap-1 text-xs font-bold text-blue-600 hover:text-blue-800 transition"
                  >
                    Acessar Painel <ArrowRight size={13} />
                  </Link>
                </div>
              </div>
            </div>

            {/* Seção: Mural de Vagas em Grupos */}
            <div className="space-y-3">
              <div className="flex justify-between items-center px-1">
                <h3 className="font-bold text-base text-slate-900">
                  Mural de Vagas em Grupos
                </h3>
                <button
                  type="button"
                  className="text-xs text-slate-500 hover:text-slate-800 transition"
                >
                  Atualizar
                </button>
              </div>

              {/* Card Vaga 1 */}
              <div className="bg-white rounded-2xl border border-slate-200/80 p-5 shadow-xs space-y-3">
                <div className="flex justify-between items-start text-xs">
                  <span className="font-bold text-slate-800">
                    Grupo de Pedro - Sistemas de Informação
                  </span>
                  <span className="text-slate-400 text-[11px]">há 2 horas</span>
                </div>

                <h4 className="font-bold text-base text-slate-900 leading-snug">
                  Procurando 1 integrante para Front-End (React/Tailwind)
                </h4>

                <p className="text-xs text-slate-500 leading-relaxed">
                  Trabalho de PI Interdisciplinar. O grupo já tem foco em estrutura mecânica, faltando dev web.
                </p>

                <div className="pt-1 flex items-center justify-end gap-2.5">
                  <button
                    type="button"
                    className="px-4 py-2 bg-slate-900 hover:bg-slate-800 text-white rounded-lg text-xs font-bold transition cursor-pointer"
                  >
                    Saber Mais
                  </button>
                  <button
                    type="button"
                    className="px-4 py-2 bg-slate-900 hover:bg-slate-800 text-white rounded-lg text-xs font-bold transition cursor-pointer"
                  >
                    Ver Compatibilidade
                  </button>
                </div>
              </div>

              {/* Card Vaga 2 */}
              <div className="bg-white rounded-2xl border border-slate-200/80 p-5 shadow-xs space-y-3">
                <div className="flex justify-between items-start text-xs">
                  <span className="font-bold text-slate-800">
                    Grupo de Maria - Design
                  </span>
                  <span className="text-slate-400 text-[11px]">há 18 horas</span>
                </div>

                <h4 className="font-bold text-base text-slate-900 leading-snug">
                  Procurando 1 integrante para implementação na nuvem
                </h4>

                <p className="text-xs text-slate-500 leading-relaxed">
                  Trabalho de PI Interdisciplinar. O grupo precisa de alguém para integrar na nuvem para fins de processamento de dados.
                </p>

                <div className="pt-1 flex items-center justify-end gap-2.5">
                  <button
                    type="button"
                    className="px-4 py-2 bg-slate-900 hover:bg-slate-800 text-white rounded-lg text-xs font-bold transition cursor-pointer"
                  >
                    Saber Mais
                  </button>
                  <button
                    type="button"
                    className="px-4 py-2 bg-slate-900 hover:bg-slate-800 text-white rounded-lg text-xs font-bold transition cursor-pointer"
                  >
                    Ver Compatibilidade
                  </button>
                </div>
              </div>

            </div>
          </div>

          {/* =========================================
              COLUNA 3: ATALHOS UNIVERSITÁRIOS (3 cols)
             ========================================= */}
          <div className="lg:col-span-3">
            <div className="bg-white rounded-2xl border border-slate-200/80 p-4 shadow-xs">
              <div className="flex items-center gap-2 mb-3 px-1 text-slate-900 font-bold text-sm">
                <span className="text-blue-600">🔗</span>
                <h3>Atalhos universitários</h3>
              </div>

              <div className="space-y-1.5">
                {atalhosUniversitarios.map((item) => (
                  <a
                    key={item.nome}
                    href={item.href}
                    target="_blank"
                    rel="noreferrer"
                    className="flex items-center justify-between p-2.5 rounded-xl bg-slate-50 hover:bg-slate-100 text-slate-700 hover:text-slate-950 text-xs font-medium transition group"
                  >
                    <div className="flex items-center gap-2.5">
                      <span>{item.icone}</span>
                      <span>{item.nome}</span>
                    </div>
                    <ExternalLink
                      size={13}
                      className="text-slate-400 group-hover:text-slate-600 transition"
                    />
                  </a>
                ))}
              </div>
            </div>
          </div>

        </div>
      </main>
    </div>
  );
}