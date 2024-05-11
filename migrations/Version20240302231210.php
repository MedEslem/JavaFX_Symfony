<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20240302231210 extends AbstractMigration
{
    public function getDescription(): string
    {
        return '';
    }

    public function up(Schema $schema): void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE competition (id INT AUTO_INCREMENT NOT NULL, nom_c VARCHAR(255) NOT NULL, description_c VARCHAR(255) NOT NULL, date_c DATE NOT NULL, heure_c TIME NOT NULL, type_c VARCHAR(255) NOT NULL, jeu_c VARCHAR(255) NOT NULL, PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE equipe (id INT AUTO_INCREMENT NOT NULL, comp_id INT DEFAULT NULL, user_id INT DEFAULT NULL, nom_e VARCHAR(255) NOT NULL, nb_p INT NOT NULL, INDEX IDX_2449BA154D0D3BCB (comp_id), INDEX IDX_2449BA15A76ED395 (user_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE equipe ADD CONSTRAINT FK_2449BA154D0D3BCB FOREIGN KEY (comp_id) REFERENCES competition (id) ON DELETE CASCADE');
        $this->addSql('ALTER TABLE equipe ADD CONSTRAINT FK_2449BA15A76ED395 FOREIGN KEY (user_id) REFERENCES `user` (id)');
    }

    public function down(Schema $schema): void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE equipe DROP FOREIGN KEY FK_2449BA154D0D3BCB');
        $this->addSql('ALTER TABLE equipe DROP FOREIGN KEY FK_2449BA15A76ED395');
        $this->addSql('DROP TABLE competition');
        $this->addSql('DROP TABLE equipe');
    }
}
