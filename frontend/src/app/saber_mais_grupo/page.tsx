"use client";

import React, { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import {
  ArrowLeft,
  Users,
  Clock,
  BookOpen,
  Calendar,
  CheckCircle2,
  Send,
  User,
  Sparkles,
} from "lucide-react";

export default function DetalhesVagaPage() {
  const router = useRouter();
  const [candidaturaEnviada, setCandidaturaEnviada] = useState(false);
  const [mensagem, setMensagem] = useState("");

  const handleCandidatar = (e: React.FormEvent) => {
    e.preventDefault();
    setCandidaturaEnviada(true);
  };

  return (
    <div className="min-h-screen bg-[#F0F4F8] flex flex-col font-sans text-slate-800">
      {/* 1. Header Superior Institucional */}
      <header className="w-full px-6 sm:px-10 py-5 flex items-center justify-between">
        <Link
          href="/"
          className="flex items-center group transition-opacity hover:opacity-90"
        >
          <Image
            src="/logo_CampusHub_sem_fundo.png"
            alt="Logo CampusHub"
            width={160}
            height={40}
            className="h-9 w-auto object-contain"
            priority
          />
        </Link>
        <span className="text-sm font-medium text-slate-900">
          Instituto Mauá de Tecnologia
        </span>
      </header>

      {/* 2. Container Central */}
      <main className="flex-1 flex items-center justify-center p-4 pb-16">
        <div className="w-full max-w-2xl bg-white rounded-3xl border border-slate-300 shadow-sm p-7 sm:p-10 relative">
          
          {/* Topo do Card / Navegação de Volta */}
          <div className="flex justify-between items-start pb-4 border-b border-slate-200">
            <div>
              <div className="inline-flex items-center gap-2 px-2.5 py-1 rounded-md bg-blue-100 text-blue-900 text-[11px] font-bold mb-2">
                <span>Vaga Aberta • 1 integrante</span>
              </div>
              <h1 className="text-2xl font-bold text-slate-950">
                Front-End (React / Tailwind CSS)
              </h1>
              <p className="text-xs text-slate-500 mt-0.5">
                Publicado há 2 horas por Pedro • Sistemas de Informação
              </p>
            </div>

            <button
              type="button"
              onClick={() => router.back()}
              className="inline-flex items-center gap-1.5 px-3.5 py-1.5 rounded-full border border-sky-400 bg-sky-50 text-sky-700 hover:bg-sky-100 text-xs font-semibold transition cursor-pointer"
            >
              <ArrowLeft size={13} />
              Voltar ao mural
            </button>
          </div>

          {/* Dados do Grupo / Projeto */}
          <div className="mt-6 space-y-6">
            
            {/* Bloco Resumo do Projeto */}
            <div className="w-full bg-[#E5E7EB]/80 rounded-2xl p-5 space-y-3">
              <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2 border-b border-slate-300/60 pb-3">
                <div>
                  <span className="text-[10px] font-bold tracking-wider uppercase text-slate-500">
                    Nome do Projeto
                  </span>
                  <h2 className="text-base font-bold text-slate-900">
                    SmartCampus - Monitoramento Inteligente
                  </h2>
                </div>
                <div className="text-left sm:text-right">
                  <span className="text-[10px] font-bold tracking-wider uppercase text-slate-500">
                    Disciplina / Contexto
                  </span>
                  <p className="text-xs font-semibold text-slate-800">
                    Projeto Integrador Interdisciplinar (PI)
                  </p>
                </div>
              </div>

              {/* Grid de Metadados rápidos */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-1 text-xs">
                <div className="flex items-center gap-2 text-slate-700">
                  <Users size={16} className="text-blue-900" />
                  <span>
                    <strong>Capacidade:</strong> 3 de 4 integrantes (Padrão PI)
                  </span>
                </div>
                <div className="flex items-center gap-2 text-slate-700">
                  <Clock size={16} className="text-blue-900" />
                  <span>
                    <strong>Disponibilidade:</strong> Período Noturno (Após 18h/ EAD)
                  </span>
                </div>
              </div>
            </div>

            {/* Descrição do Projeto */}
            <div>
              <h3 className="text-xs font-bold text-slate-800 tracking-wide uppercase mb-2">
                Sobre o Projeto e o Escopo
              </h3>
              <div className="bg-[#E5E7EB]/50 rounded-2xl p-4 text-xs sm:text-sm text-slate-700 leading-relaxed border border-slate-200/60">
                Nosso trabalho interdisciplinar de PI foca no desenvolvimento de um sistema de sensores para captação de métricas de ocupação nos laboratórios da Mauá. Já possuímos a infraestrutura mecânica, modelagem e firmware em andamento, mas precisamos de um colega focado em construir o painel web responsivo (dashboard) em React e Tailwind CSS para apresentação nas bancas.
              </div>
            </div>

            {/* Competências que o Grupo Já Possui */}
            <div>
              <h3 className="text-xs font-bold text-slate-800 tracking-wide uppercase mb-2">
                Competências que o Grupo Já Possui
              </h3>
              <div className="w-full bg-[#E5E7EB]/80 rounded-2xl px-4 py-3 flex flex-wrap items-center gap-2">
                {["C/C++", "Arduino", "ESP32", "Modelagem 3D (CAD/SolidWorks)"].map((tag) => (
                  <span
                    key={tag}
                    className="inline-flex items-center gap-1.5 bg-[#93C5FD] text-slate-950 font-medium text-xs px-3 py-1.5 rounded-xl shadow-xs"
                  >
                    {tag}
                  </span>
                ))}
              </div>
            </div>

            {/* Competências Procuradas (Vagas) */}
            <div>
              <h3 className="text-xs font-bold text-[#F87171] tracking-wide uppercase mb-2">
                Competências que o Grupo Está Procurando (Vaga)
              </h3>
              <div className="w-full bg-[#FCA5A5]/45 rounded-2xl px-4 py-3 flex flex-wrap items-center gap-2">
                {["React", "Tailwind CSS", "JavaScript"].map((tag) => (
                  <span
                    key={tag}
                    className="inline-flex items-center gap-1.5 bg-[#F87171] text-slate-950 font-semibold text-xs px-3 py-1.5 rounded-xl shadow-xs"
                  >
                    {tag}
                  </span>
                ))}
              </div>
            </div>

            {/* Integrantes Atuais */}
            <div>
              <h3 className="text-xs font-bold text-slate-800 tracking-wide uppercase mb-2">
                Integrantes Atuais (3)
              </h3>
              <div className="grid grid-cols-1 sm:grid-cols-3 gap-2.5">
                {[
                  { nome: "Pedro", curso: "Sistemas de Info." },
                  { nome: "Gabriel", curso: "Engenharia Mecânica" },
                  { nome: "Lucas", curso: "Engenharia Elétrica" },
                ].map((membro, i) => (
                  <div
                    key={i}
                    className="flex items-center gap-3 p-2.5 bg-slate-50 border border-slate-200 rounded-xl"
                  >
                    <div className="w-8 h-8 rounded-full bg-[#1D3557] text-white flex items-center justify-center shrink-0">
                      <User size={16} />
                    </div>
                    <div className="truncate">
                      <p className="text-xs font-bold text-slate-900 truncate">
                        {membro.nome}
                      </p>
                      <p className="text-[10px] text-slate-500 truncate">
                        {membro.curso}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* 3. Seção de Candidatura */}
            <div className="pt-4 border-t border-slate-200">
              {candidaturaEnviada ? (
                <div className="bg-emerald-50 border border-emerald-300 rounded-2xl p-6 text-center space-y-2">
                  <div className="w-12 h-12 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center mx-auto">
                    <CheckCircle2 size={24} />
                  </div>
                  <h4 className="text-base font-bold text-emerald-950">
                    Solicitação enviada com sucesso!
                  </h4>
                  <p className="text-xs text-emerald-800 max-w-sm mx-auto">
                    O líder do grupo (Pedro) recebeu seu interesse com os dados do seu perfil base. Fique atento às suas notificações!
                  </p>
                  <button
                    type="button"
                    onClick={() => router.push("/")}
                    className="mt-3 text-xs font-bold text-emerald-900 underline hover:text-emerald-700 cursor-pointer"
                  >
                    Voltar ao Hub inicial
                  </button>
                </div>
              ) : (
                <form onSubmit={handleCandidatar} className="space-y-4">
                  <div>
                    <label
                      htmlFor="mensagem"
                      className="block text-xs font-bold text-slate-800 tracking-wide uppercase mb-1.5"
                    >
                      Mensagem de Apresentação (Opcional)
                    </label>
                    <textarea
                      id="mensagem"
                      rows={3}
                      value={mensagem}
                      onChange={(e) => setMensagem(e.target.value)}
                      placeholder="Olá! Sou estudante de Engenharia/TI, tenho experiência com React e gostaria muito de somar ao grupo de vocês..."
                      className="w-full bg-[#E5E7EB]/80 text-slate-800 placeholder-slate-500 rounded-2xl p-4 text-xs sm:text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition resize-none"
                    />
                  </div>

                  <div className="flex flex-col sm:flex-row items-center justify-between gap-3 pt-2">
                    <div className="flex items-center gap-2 text-xs text-slate-500">
                      <Sparkles size={15} className="text-blue-600" />
                      <span>Seu perfil base (habilidades e contato) será compartilhado.</span>
                    </div>

                    <button
                      type="submit"
                      className="w-full sm:w-auto px-8 py-3.5 bg-[#2563EB] hover:bg-blue-700 text-white font-bold text-xs sm:text-sm rounded-full transition shadow-md hover:shadow-lg active:scale-[0.99] cursor-pointer flex items-center justify-center gap-2"
                    >
                      <Send size={15} /> Solicitar Entrada no Grupo
                    </button>
                  </div>
                </form>
              )}
            </div>

          </div>
        </div>
      </main>
    </div>
  );
}