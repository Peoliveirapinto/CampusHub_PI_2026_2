import Link from "next/link";
import { 
  LogIn, 
  Users, 
  Boxes, 
  Network 
} from "lucide-react";

export default function Home() {
  return (
    <div className="bg-slate-50 text-gray-800 font-sans antialiased min-h-screen">
      
      {/* 1. NAVBAR / CABEÇALHO */}
      <nav className="bg-white/80 backdrop-blur-md border-b border-gray-200 fixed w-full top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          
          {/* Logo */}
          <div className="flex items-center gap-3">
            <div className="bg-blue-900 text-white font-bold px-3 py-1.5 rounded-xl text-lg tracking-wide">
              CH
            </div>
            <span className="font-bold text-gray-900 text-xl">CampusHub</span>
          </div>

          {/* Links de Navegação */}
          <div className="hidden md:flex items-center gap-8 text-sm font-medium text-gray-600">
            <a href="#recursos" className="hover:text-blue-900 transition">
              Recursos
            </a>
            <a href="#como-funciona" className="hover:text-blue-900 transition">
              Como Funciona
            </a>
            <a href="#sobre" className="hover:text-blue-900 transition">
              Sobre o Projeto
            </a>
          </div>

          {/* Botão de Acesso */}
          <div className="flex items-center gap-3">
            <Link
              href="/login"
              className="px-5 py-2.5 bg-blue-900 hover:bg-blue-800 text-white font-semibold text-sm rounded-xl transition shadow-sm hover:shadow"
            >
              Acessar Plataforma &rarr;
            </Link>
          </div>

        </div>
      </nav>

      {/* 2. HERO SECTION */}
      <section className="pt-32 pb-20 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto text-center">
        
        {/* Badge */}
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-lime-100 border border-lime-300 text-lime-900 text-xs font-semibold mb-6">
          <span className="w-2 h-2 rounded-full bg-lime-600 animate-pulse"></span>
          Exclusivo para estudantes do Instituto Mauá de Tecnologia
        </div>

        {/* Título Principal */}
        <h1 className="text-4xl sm:text-6xl font-extrabold text-gray-900 tracking-tight max-w-4xl mx-auto leading-tight">
          Sua vida universitária na Mauá,{" "}
          <span className="text-blue-900">centralizada e conectada.</span>
        </h1>

        {/* Subtítulo */}
        <p className="mt-6 text-lg sm:text-xl text-gray-600 max-w-2xl mx-auto leading-relaxed">
          Encontre parceiros de trabalho por compatibilidade de habilidades, acesse suas ferramentas essenciais e acabe com o estresse de montar grupos de PI.
        </p>

        {/* Botões de Ação (CTA) */}
        <div className="mt-8 flex flex-col sm:flex-row gap-4 justify-center items-center">
          <Link
            href="/login"
            className="w-full sm:w-auto px-8 py-3.5 bg-blue-900 hover:bg-blue-800 text-white font-bold text-base rounded-xl transition shadow-lg hover:shadow-blue-900/20 flex items-center justify-center gap-2"
          >
            <LogIn size={18} /> Entrar com e-mail @maua.br
          </Link>
          <a
            href="#como-funciona"
            className="w-full sm:w-auto px-8 py-3.5 bg-white border border-gray-300 hover:bg-gray-50 text-gray-700 font-semibold text-base rounded-xl transition text-center"
          >
            Conhecer o projeto
          </a>
        </div>

      </section>

      {/* 3. SEÇÃO DE RECURSOS */}
      <section id="recursos" className="py-16 bg-white border-y border-gray-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          
          <div className="text-center max-w-2xl mx-auto mb-12">
            <h2 className="text-3xl font-bold text-gray-900">Por que usar o CampusHub?</h2>
            <p className="text-gray-600 text-sm mt-2">
              Criado por alunos que conhecem de perto a rotina e os desafios da Mauá.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            
            {/* Card 1 */}
            <div className="p-6 bg-slate-50 border border-gray-200 rounded-2xl hover:border-blue-300 transition">
              <div className="w-12 h-12 bg-blue-100 text-blue-900 rounded-xl flex items-center justify-center mb-4">
                <Users size={24} />
              </div>
              <h3 className="font-bold text-lg text-gray-900">Formação Inteligente de Grupos</h3>
              <p className="text-sm text-gray-600 mt-2">
                Encontre colegas com as competências complementares que seu projeto precisa (Python, CAD, Gestão, UX) em vez de dependência apenas da sorte.
              </p>
            </div>

            {/* Card 2 */}
            <div className="p-6 bg-slate-50 border border-gray-200 rounded-2xl hover:border-blue-300 transition">
              <div className="w-12 h-12 bg-lime-100 text-lime-900 rounded-xl flex items-center justify-center mb-4">
                <Boxes size={24} />
              </div>
              <h3 className="font-bold text-lg text-gray-900">Ferramentas Centralizadas</h3>
              <p className="text-sm text-gray-600 mt-2">
                Acesse seus atalhos acadêmicos principais em uma única dashboard limpa e rápida, sem precisar navegar por menus lentos.
              </p>
            </div>

            {/* Card 3 */}
            <div className="p-6 bg-slate-50 border border-gray-200 rounded-2xl hover:border-blue-300 transition">
              <div className="w-12 h-12 bg-blue-100 text-blue-900 rounded-xl flex items-center justify-center mb-4">
                <Network size={24} />
              </div>
              <h3 className="font-bold text-lg text-gray-900">Integração Interdisciplinar</h3>
              <p className="text-sm text-gray-600 mt-2">
                Conecte-se com estudantes de Engenharia, Design e Administração para montar times multidisciplinares completos.
              </p>
            </div>

          </div>

        </div>
      </section>

      {/* 4. COMO FUNCIONA */}
      <section id="como-funciona" className="py-16 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center max-w-2xl mx-auto mb-12">
          <h2 className="text-3xl font-bold text-gray-900">Como funciona em 3 passos simples</h2>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
          <div className="space-y-3">
            <div className="w-10 h-10 bg-blue-900 text-white rounded-full font-bold flex items-center justify-center mx-auto text-lg">
              1
            </div>
            <h4 className="font-bold text-gray-900">Autenticação Única</h4>
            <p className="text-xs text-gray-600">
              Acesse instantaneamente usando sua conta institucional da Mauá (@maua.br).
            </p>
          </div>

          <div className="space-y-3">
            <div className="w-10 h-10 bg-blue-900 text-white rounded-full font-bold flex items-center justify-center mx-auto text-lg">
              2
            </div>
            <h4 className="font-bold text-gray-900">Configure seu Perfil</h4>
            <p className="text-xs text-gray-600">
              Selecione seu curso e adicione suas principais habilidades e interesses de projeto.
            </p>
          </div>

          <div className="space-y-3">
            <div className="w-10 h-10 bg-lime-500 text-blue-950 rounded-full font-bold flex items-center justify-center mx-auto text-lg">
              3
            </div>
            <h4 className="font-bold text-gray-900">Conecte-se e Produza</h4>
            <p className="text-xs text-gray-600">
              Publique vagas no mural, encontre grupos abertos e gerencie suas entregas.
            </p>
          </div>
        </div>
      </section>

      {/* 5. BANNER FINAL / CALL TO ACTION */}
      <section className="py-12 bg-blue-950 text-white text-center">
        <div className="max-w-4xl mx-auto px-4">
          <h2 className="text-2xl sm:text-3xl font-bold mb-3">
            Pronto para otimizar seus projetos acadêmicos?
          </h2>
          <p className="text-blue-200 text-sm mb-6">
            Acesse agora com seu e-mail da Mauá e comece a montar sua rede.
          </p>
          <Link
            href="/login"
            className="inline-block px-8 py-3 bg-lime-400 hover:bg-lime-500 text-blue-950 font-bold text-sm rounded-xl transition shadow-md"
          >
            Acessar o CampusHub
          </Link>
        </div>
      </section>

      {/* 6. RODAPÉ */}
      <footer id="sobre" className="bg-gray-900 text-gray-400 py-8 text-xs border-t border-gray-800">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row justify-between items-center gap-4">
          <div>
            <p className="font-semibold text-white">CampusHub • Projeto Integrador Interdisciplinar</p>
            <p className="text-gray-500 mt-0.5">Instituto Mauá de Tecnologia — Campus São Caetano do Sul</p>
          </div>
          <div className="flex gap-4">
            <span>Matheus Giongo</span>
            <span>Ian Pezzuol</span>
            <span>Felipe Zanardo</span>
            <span>Pedro Pinto</span>
          </div>
        </div>
      </footer>

    </div>
  );
}