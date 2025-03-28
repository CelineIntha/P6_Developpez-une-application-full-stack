-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:8889
-- Généré le : ven. 28 mars 2025 à 09:44
-- Version du serveur : 5.7.39
-- Version de PHP : 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `mdd`
--

-- --------------------------------------------------------

--
-- Structure de la table `articles`
--

CREATE TABLE `articles` (
  `id` bigint(20) NOT NULL,
  `content` text NOT NULL,
  `createdAt` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author_id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `articles`
--

INSERT INTO `articles` (`id`, `content`, `createdAt`, `title`, `author_id`, `topic_id`) VALUES
(15, 'Le télétravail peut être un défi... Découvrez 5 conseils pratiques pour maintenir votre productivité tout au long de la journée.', '2024-12-10 10:15:00.000000', '5 astuces pour rester productif en télétravail', 1, 2),
(16, 'L\'intelligence artificielle transforme la manière dont nous concevons les sites web. Voici comment l\'IA est utilisée au quotidien par les développeurs.', '2025-01-05 09:30:00.000000', 'L\'impact de l\'IA sur le développement web', 2, 1),
(17, 'Dans cet article, nous explorons les 10 outils indispensables pour tout développeur moderne en 2025. IDE, gestionnaires de paquets, linters... tout y est.', '2025-02-20 14:45:00.000000', 'Top 10 des outils pour les développeurs en 2025', 2, 1),
(18, 'Les entreprises investissent de plus en plus dans le bien-être de leurs collaborateurs. On vous présente les meilleures initiatives vues en 2024.', '2024-11-18 08:20:00.000000', 'Bien-être au travail : les nouvelles tendances RH', 1, 2),
(19, 'Angular continue d\'évoluer et reste un framework solide pour les applications frontend. Voici pourquoi il mérite votre attention cette année.', '2025-03-01 11:05:00.000000', 'Pourquoi apprendre Angular en 2025 ?', 2, 1);

-- --------------------------------------------------------

--
-- Structure de la table `comments`
--

CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `createdAt` datetime(6) NOT NULL,
  `article_id` bigint(20) NOT NULL,
  `author_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `subscriptions`
--

CREATE TABLE `subscriptions` (
  `id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `topics`
--

CREATE TABLE `topics` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `topics`
--

INSERT INTO `topics` (`id`, `name`) VALUES
(4, 'Angular'),
(6, 'Backend'),
(5, 'Frontend'),
(1, 'Java'),
(2, 'Spring Boot'),
(3, 'UX Design');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `email`, `password`, `username`) VALUES
(1, 'celine@email.fr', '$2a$10$TE33F24Icl3jAJ0xbistsekIxOXfjeBE3A1us48lmfUc1sAmpm4we', 'celine'),
(2, 'guillaume@email.com', '$2a$10$3xXHvwaAPivlnle3aBA9g.V1eoRrcRPcKalNKtTfcJ9VHHdP2LAdy', 'guillaume'),
(3, 'john@email.com', '$2a$10$5K.o9aA8M.rW3Xn4KN/hEeQ3yroSYO7myT45hJo8SX1AC1F/kZV2a', 'john');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `articles`
--
ALTER TABLE `articles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKe02fs2ut6qqoabfhj325wcjul` (`author_id`),
  ADD KEY `FKtr90v51q71w7rpslscsfjf3cv` (`topic_id`);

--
-- Index pour la table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKk4ib6syde10dalk7r7xdl0m5p` (`article_id`),
  ADD KEY `FKn2na60ukhs76ibtpt9burkm27` (`author_id`);

--
-- Index pour la table `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKibqtk993y6263yge9f70q4t3f` (`user_id`,`topic_id`),
  ADD KEY `FKfuag8et2vdg3ds9h8trqx2ldq` (`topic_id`);

--
-- Index pour la table `topics`
--
ALTER TABLE `topics`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK7tuhnscjpohbffmp7btit1uff` (`name`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `articles`
--
ALTER TABLE `articles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT pour la table `comments`
--
ALTER TABLE `comments`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `subscriptions`
--
ALTER TABLE `subscriptions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `topics`
--
ALTER TABLE `topics`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `articles`
--
ALTER TABLE `articles`
  ADD CONSTRAINT `FKe02fs2ut6qqoabfhj325wcjul` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKtr90v51q71w7rpslscsfjf3cv` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`);

--
-- Contraintes pour la table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `FKk4ib6syde10dalk7r7xdl0m5p` FOREIGN KEY (`article_id`) REFERENCES `articles` (`id`),
  ADD CONSTRAINT `FKn2na60ukhs76ibtpt9burkm27` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);

--
-- Contraintes pour la table `subscriptions`
--
ALTER TABLE `subscriptions`
  ADD CONSTRAINT `FKfuag8et2vdg3ds9h8trqx2ldq` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`id`),
  ADD CONSTRAINT `FKhro52ohfqfbay9774bev0qinr` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
