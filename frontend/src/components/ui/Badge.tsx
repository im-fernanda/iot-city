import React from 'react';

interface BadgeProps {
  children: React.ReactNode;
  variant?: 'success' | 'danger' | 'warning' | 'info' | 'default';
  className?: string;
}

export const Badge: React.FC<BadgeProps> = ({ 
  children, 
  variant = 'default',
  className = '' 
}) => {
  const baseClasses = "inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-wide backdrop-blur-md";
  
  const variants = {
    success: "bg-green-500/20 text-green-300 border border-green-400/30",
    danger: "bg-red-500/20 text-red-300 border border-red-400/30",
    warning: "bg-yellow-500/20 text-yellow-300 border border-yellow-400/30",
    info: "bg-blue-500/20 text-blue-300 border border-blue-400/30",
    default: "bg-white/10 text-white/80 border border-white/20",
  };

  return (
    <span className={`${baseClasses} ${variants[variant]} ${className}`}>
      {children}
    </span>
  );
};

