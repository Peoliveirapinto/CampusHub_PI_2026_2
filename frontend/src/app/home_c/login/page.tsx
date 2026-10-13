"use client";

import React, { useState, useEffect, useMemo, useRef } from "react";
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
  CheckCheck,
} from "lucide-react";

// Tipagens dos contratos de dados (DTOs)
interface PerfilUsuario {
  id: number;
  nome: string;
  curso: string;
  semestre: number;
  habilidades: string[];
}

interface Integrante {
  id: number;
  nome: string;
}

interface GrupoAtivo {
  id: number;
  nome: string;
  categoria: string;
  descricao: string;
  vagasOcupadas: number;
  vagasTotais: number;
  integrantes: Integrante[];
}

interface VagaMural {
  id: number;
  autorGrupo: string;
  cursoLider: string;
  titulo: string;
  tempoPublicacao: string;
  descricao: string;
  habilidadesRequeridas: string[];
}

interface NotificacaoItem {
  id: number;
  titulo: string;
  descricao: string;
  tempo: string;
  lida: boolean;
}

export default function HubPage() {
  const router = useRouter();

  // Estados com dados iniciais (Mock)
  const [usuario, setUsuario] = useState<PerfilUsuario>({
    id: 1,
    nome: "Mario Bros",
    curso: "Engenharia",
    semestre: 5,
    habilidades: ["Python", "Docker", "MongoDB"],
  });

  const [gruposAtivos, setGruposAtivos] = useState<GrupoAtivo[]>([
    {
      id: 10,
      nome: "CampusHub",
      categoria: "Projeto Integrador",
      descricao: "Desenvolvimento do protótipo e documentação",
      vagasOcupadas: 5,
      vagasTotais: 6,
      integrantes: [
        { id: 1, nome: "Mario" },
        { id: 2, nome: "Ian" },
        { id: 3, nome: "Pedro" },
        { id: 4, nome: "Matheus" },
        { id: 5, nome: "Felipe" },
      ],
    },
  ]);

  const [vagas, setVagas] = useState<VagaMural[]>([
    {
      id: 101,
      autorGrupo: "Grupo de Pedro",
      cursoLider: "Sistemas de Informação",
      titulo: "Procurando 1 integrante para Front-End (React/Tailwind)",
      tempoPublicacao: "há 2 horas",
      descricao:
        "Trabalho de PI Interdisciplinar. O grupo já tem foco em estrutura mecânica, faltando dev web.",
      habilidadesRequeridas: ["React", "Tailwind CSS", "JavaScript"],
    },
    {
      id: 102,
      autorGrupo: "Grupo de Maria",
      cursoLider: "Design",
      titulo: "Procurando 1 integrante para implementação na nuvem",
      tempoPublicacao: "há 18 horas",
      descricao:
        "Trabalho de PI Interdisciplinar. O grupo precisa de alguém para integrar na nuvem para fins de processamento de dados.",
      habilidadesRequeridas: ["AWS", "Docker", "Python"],
    },
  ]);

  // Estados das Notificações
  const [notificacoesAbertas, setNotificacoesAbertas] = useState(false);
  const [notificacoes, setNotificacoes] = useState<NotificacaoItem[]>([
    {
      id: 1,
      titulo: "Nova solicitação de entrada",
      descricao: "Lucas (Eng. Elétrica) solicitou entrada no grupo CampusHub.",
      tempo: "há 10 min",
      lida: false,
    },
    {
      id: 2,
      titulo: "Candidatura aceita!",
      descricao: "Pedro aceitou você no grupo de Sistemas de Informação.",
      tempo: "há 1 hora",
      lida: false,
    },
    {
      id: 3,
      titulo: "Novo match de habilidades",
      descricao: "Há uma nova vaga procurando exatamente Python e Docker.",
      tempo: "ontem",
      lida: true,
    },
  ]);

  const [busca, setBusca] = useState<string>("");
  const popoverNotificacoesRef = useRef<HTMLDivElement>(null);

  // Fecha o dropdown de notificações se clicar fora dele
  useEffect(() => {
    function handleClickFora(e: MouseEvent) {
      if (
        popoverNotificacoesRef.current &&
        !popoverNotificacoesRef.current.contains(e.target as Node)
      ) {
        setNotificacoesAbertas(false);
      }
    }
    document.addEventListener("mousedown", handleClickFora);
    return () => document.removeEventListener("mousedown", handleClickFora);
  }, []);

  const totalNaoLidas = useMemo(
    () => notificacoes.filter((n) => !n.lida).length,
    [notificacoes]
  );

  const marcarTodasComoLidas = () => {
    setNotificacoes((prev) => prev.map((item) => ({ ...item, lida: true })));
  };

  // Atalhos institucionais fixos
  const atalhosUniversitarios = [
    { nome: "Eureka", href: "https://eureka.maua.br", icone: "💡" },
    { nome: "Calendário Acadêmico", href: "#", icone: "📅" },
    { nome: "Entidades Acadêmicas", href: "#", icone: "👥" },
    { nome: "Intercâmbio & RI", href: "#", icone: "🌎" },
    { nome: "RU / Cardápio", href: "#", icone: "🍽️" },
  ];

  // Filtro de vagas em tempo real pela barra de pesquisa
  const vagasFiltradas = useMemo(() => {
    if (!busca.trim()) return vagas;
    const termo = busca.toLowerCase();
    return vagas.filter(
      (vaga) =>
        vaga.titulo.toLowerCase().includes(termo) ||
        vaga.descricao.toLowerCase().includes(termo) ||
        vaga.habilidadesRequeridas.some((h) => h.toLowerCase().includes(termo))
    );
  }, [vagas, busca]);

  // Cálculo da compatibilidade com o perfil
  const verificarCompatibilidade = (habilidadesVaga: string[]) => {
    const correspondencias = habilidadesVaga.filter((hab) =>
      usuario.habilidades.some(
        (uHab) => uHab.toLowerCase() === hab.toLowerCase()
      )
    );
    const percentual = Math.round(
      (correspondencias.length / habilidadesVaga.length) * 100
    );

    if (correspondencias.length > 0) {
      alert(
        `Compatibilidade: ${percentual}%\nCompetências coincidentes: ${correspondencias.join(
          ", "
        )}`
      );
    } else {
      alert(
        "Ainda não possui as competências solicitadas por este grupo no seu perfil base."
      );
    }
  };

  return (
    <div className="min-h-screen bg-[#F0F4F8] text-slate-800 font-sans flex flex-col">
      {/* 1. NAVBAR SUPERIOR LOGADA */}
      <nav className="sticky top-0 z-40 bg-white border-b border-slate-200 px-4 sm:px-8 h-16 flex items-center justify-between gap-4">
        {/* Lado Esquerdo: Logótipo PNG */}
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

        {/* Centro: Barra de Pesquisa */}
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
              placeholder="Buscar alunos por habilidade (ex: Python, UX/UI, C++)"
              className="w-full bg-[#E5E7EB]/70 hover:bg-[#E5E7EB] focus:bg-white text-xs sm:text-sm text-slate-800 placeholder-slate-500 rounded-xl pl-10 pr-4 py-2 outline-none border border-transparent focus:border-slate-300 transition"
            />
          </div>
        </div>

        {/* Lado Direito: Notificações + Perfil do Aluno */}
        <div className="flex items-center gap-3 sm:gap-6 shrink-0">
          
          {/* Sino de Notificações com Dropdown Flutuante */}
          <div className="relative" ref={popoverNotificacoesRef}>
            <button
              type="button"
              onClick={() => setNotificacoesAbertas(!notificacoesAbertas)}
              className="relative p-2 text-slate-600 hover:text-slate-900 transition rounded-xl hover:bg-slate-100 cursor-pointer"
              title="Notificações"
            >
              <Bell size={20} />
              {/* Badge pulsante com contagem */}
              {totalNaoLidas > 0 && (
                <span className="absolute top-1.5 right-1.5 flex h-2.5 w-2.5">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-red-500 ring-2 ring-white"></span>
                </span>
              )}
            </button>

            {/* Menu Flutuante de Notificações */}
            {notificacoesAbertas && (
              <div className="absolute right-0 mt-2 w-80 sm:w-96 bg-white rounded-2xl shadow-xl border border-slate-200 p-4 z-50 animate-in fade-in zoom-in-95 duration-150">
                <div className="flex items-center justify-between pb-3 border-b border-slate-100">
                  <div className="flex items-center gap-2">
                    <span className="font-bold text-sm text-slate-900">
                      Notificações
                    </span>
                    {totalNaoLidas > 0 && (
                      <span className="bg-blue-100 text-blue-700 text-[11px] font-bold px-2 py-0.5 rounded-full">
                        {totalNaoLidas} nova{totalNaoLidas > 1 ? "s" : ""}
                      </span>
                    )}
                  </div>
                  {totalNaoLidas > 0 && (
                    <button
                      type="button"
                      onClick={marcarTodasComoLidas}
                      className="text-[11px] font-semibold text-blue-600 hover:text-blue-800 transition flex items-center gap-1 cursor-pointer"
                    >
                      <CheckCheck size={14} /> Marcar lidas
                    </button>
                  )}
                </div>

                {/* Lista de Notificações */}
                <div className="mt-2 max-h-80 overflow-y-auto space-y-1.5 divide-y divide-slate-100 pr-1">
                  {notificacoes.length > 0 ? (
                    notificacoes.map((item) => (
                      <div
                        key={item.id}
                        className={`pt-2.5 first:pt-1 pb-1.5 px-2.5 rounded-xl transition ${
                          !item.lida ? "bg-blue-50/60" : "hover:bg-slate-50"
                        }`}
                      >
                        <div className="flex justify-between items-start gap-2">
                          <h4 className="text-xs font-bold text-slate-900">
                            {item.titulo}
                          </h4>
                          <span className="text-[10px] text-slate-400 shrink-0">
                            {item.tempo}
                          </span>
                        </div>
                        <p className="text-xs text-slate-600 mt-0.5 leading-relaxed">
                          {item.descricao}
                        </p>
                      </div>
                    ))
                  ) : (
                    <div className="py-6 text-center text-xs text-slate-400">
                      Nenhuma notificação no momento.
                    </div>
                  )}
                </div>

                <div className="mt-3 pt-2.5 border-t border-slate-100 text-center">
                  <button
                    type="button"
                    onClick={() => setNotificacoesAbertas(false)}
                    className="text-xs font-bold text-slate-600 hover:text-blue-900 transition cursor-pointer"
                  >
                    Fechar
                  </button>
                </div>
              </div>
            )}
          </div>

          <div className="h-7 w-px bg-slate-200 hidden sm:block"></div>

          {/* Card do Perfil do Aluno */}
          <Link
            href="/editar_perfil"
            className="flex items-center gap-3 group cursor-pointer"
          >
            <div className="w-10 h-10 rounded-full bg-[#2563EB] text-white flex items-center justify-center shrink-0 group-hover:ring-2 group-hover:ring-blue-400 transition">
              <User size={24} />
            </div>
            <div className="hidden md:flex flex-col text-left">
              <span className="text-sm font-bold text-slate-900 group-hover:text-blue-900 transition">
                {usuario.nome}
              </span>
              <span className="text-[11px] text-slate-500 font-medium">
                {usuario.curso} - IMT
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
                    {gruposAtivos.length}
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

            {/* SEU PERFIL BASE */}
            <div className="bg-[#0B0D63] text-white rounded-2xl p-5 shadow-sm space-y-3">
              <p className="text-[11px] font-black uppercase tracking-wider text-lime-400">
                Seu Perfil Base
              </p>
              <div>
                <h3 className="font-bold text-base">{usuario.nome}</h3>
                <p className="text-xs text-blue-200 mt-0.5">
                  {usuario.curso} - {usuario.semestre}º Semestre
                </p>
              </div>

              <div className="h-px bg-white/15 my-2"></div>

              <div className="flex flex-wrap gap-1.5 pt-1">
                {usuario.habilidades.map((tech) => (
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
            
            {/* Banner Chamada PI */}
            <div className="bg-white rounded-2xl border border-slate-200/80 p-6 shadow-xs flex flex-col md:flex-row md:items-center justify-between gap-4">
              <div className="max-w-xs">
                <h2 className="text-xl font-bold text-slate-900 tracking-tight">
                  Procurando grupo para PI?
                </h2>
                <p className="text-xs text-slate-500 mt-1 leading-relaxed">
                  Crie o seu grupo definindo as competências em falta ou procure matches complementares.
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

            {/* Meus Grupos Ativos */}
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

              {gruposAtivos.map((grupo) => (
                <div
                  key={grupo.id}
                  className="bg-white rounded-2xl border border-slate-200/80 p-5 shadow-xs relative"
                >
                  <div className="flex justify-between items-start">
                    <span className="px-2.5 py-0.5 rounded-md bg-blue-100 text-blue-700 text-[11px] font-bold">
                      {grupo.categoria}
                    </span>
                    <span className="text-xs font-bold text-amber-600">
                      {grupo.vagasOcupadas}/{grupo.vagasTotais} alunos
                    </span>
                  </div>

                  <h4 className="font-bold text-lg text-slate-900 mt-2">
                    {grupo.nome}
                  </h4>
                  <p className="text-xs text-slate-500 mt-0.5">
                    {grupo.descricao}
                  </p>

                  <div className="h-px bg-slate-100 my-4"></div>

                  <div className="flex items-center justify-between">
                    <div className="flex items-center -space-x-2">
                      {grupo.integrantes.map((membro) => (
                        <div
                          key={membro.id}
                          title={membro.nome}
                          className="w-8 h-8 rounded-full bg-[#1D3557] border-2 border-white text-white flex items-center justify-center text-xs font-bold shadow-2xs"
                        >
                          <User size={16} />
                        </div>
                      ))}
                    </div>

                    <Link
                      href={`/grupo/${grupo.id}`}
                      className="inline-flex items-center gap-1 text-xs font-bold text-blue-600 hover:text-blue-800 transition"
                    >
                      Acessar Painel <ArrowRight size={13} />
                    </Link>
                  </div>
                </div>
              ))}
            </div>

            {/* Mural de Vagas em Grupos */}
            <div className="space-y-3">
              <div className="flex justify-between items-center px-1">
                <h3 className="font-bold text-base text-slate-900">
                  Mural de Vagas em Grupos
                </h3>
                <span className="text-xs text-slate-400">
                  {vagasFiltradas.length} vaga(s) disponível(eis)
                </span>
              </div>

              {vagasFiltradas.length === 0 ? (
                <div className="bg-white rounded-2xl border border-slate-200 p-8 text-center text-slate-500 text-xs">
                  Nenhuma vaga encontrada para &quot;{busca}&quot;.
                </div>
              ) : (
                vagasFiltradas.map((vaga) => (
                  <div
                    key={vaga.id}
                    className="bg-white rounded-2xl border border-slate-200/80 p-5 shadow-xs space-y-3"
                  >
                    <div className="flex justify-between items-start text-xs">
                      <span className="font-bold text-slate-800">
                        {vaga.autorGrupo} - {vaga.cursoLider}
                      </span>
                      <span className="text-slate-400 text-[11px]">
                        {vaga.tempoPublicacao}
                      </span>
                    </div>

                    <h4 className="font-bold text-base text-slate-900 leading-snug">
                      {vaga.titulo}
                    </h4>

                    <p className="text-xs text-slate-500 leading-relaxed">
                      {vaga.descricao}
                    </p>

                    <div className="flex flex-wrap gap-1.5 pt-1">
                      {vaga.habilidadesRequeridas.map((tag) => (
                        <span
                          key={tag}
                          className="px-2 py-0.5 rounded-md bg-slate-100 text-slate-600 text-[10px] font-medium"
                        >
                          {tag}
                        </span>
                      ))}
                    </div>

                    <div className="pt-2 flex items-center justify-end gap-2.5">
                      <button
                        type="button"
                        onClick={() => router.push(`/detalhes_vaga?id=${vaga.id}`)}
                        className="px-4 py-2 bg-slate-900 hover:bg-slate-800 text-white rounded-lg text-xs font-bold transition cursor-pointer"
                      >
                        Saber Mais
                      </button>
                      <button
                        type="button"
                        onClick={() =>
                          verificarCompatibilidade(vaga.habilidadesRequeridas)
                        }
                        className="px-4 py-2 bg-slate-900 hover:bg-slate-800 text-white rounded-lg text-xs font-bold transition cursor-pointer"
                      >
                        Ver Compatibilidade
                      </button>
                    </div>
                  </div>
                ))
              )}
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