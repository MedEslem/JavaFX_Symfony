<?php

namespace App\Entity;

use App\Repository\ProduitRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: ProduitRepository::class)]
class Produit
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id_produit = null;

    #[ORM\Column(length: 50)]
    #[Assert\NotBlank(message:"nom is required")]
    #[Assert\Length(min:3,minMessage:"Votre nom de produit ne contient pas au minimum {{ limit }} caractères.")]
    private ?string $nom_produit = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message:"description is required")]
    #[Assert\Length(max:10000,maxMessage:"Votre description contient plus {{ limit }} caractères.")]
    private ?string $description_produit = null;

    #[ORM\Column(length: 255)]
    private ?string $image_produit = null;

    #[ORM\Column(type: Types::DECIMAL, precision: 10, scale: '0')]
    #[Assert\GreaterThan(value:0 ,message:"Prix doit être un nombre positif.")]
    #[Assert\NotBlank(message:"price is required")]

    private ?string $prix_produit = null;

    #[ORM\ManyToOne(inversedBy: 'produits')]
    #[ORM\JoinColumn(nullable: false,referencedColumnName:"id_categ_prod",onDelete:"CASCADE")]
    private ?CategorieProd $categorieProd = null;

    #[ORM\ManyToOne(inversedBy: 'produits')]
    #[ORM\JoinColumn(nullable: false,referencedColumnName:"id_tag",onDelete:"CASCADE")]
    private ?Tags $Tags = null;


    public function getIdProduit(): ?int
    {
        return $this->id_produit;
    }

    public function getNomProduit(): ?string
    {
        return $this->nom_produit;
    }

    public function setNomProduit(string $nom_produit): static
    {
        $this->nom_produit = $nom_produit;

        return $this;
    }

    public function getDescriptionProduit(): ?string
    {
        return $this->description_produit;
    }

    public function setDescriptionProduit(string $description_produit): static
    {
        $this->description_produit = $description_produit;

        return $this;
    }

    public function getImageProduit(): ?string
    {
        return $this->image_produit;
    }

    public function setImageProduit(string $image_produit): static
    {
        $this->image_produit = $image_produit;

        return $this;
    }

    public function getPrixProduit(): ?string
    {
        return $this->prix_produit;
    }

    public function setPrixProduit(string $prix_produit): static
    {
        $this->prix_produit = $prix_produit;

        return $this;
    }

    public function getCategorieProd(): ?CategorieProd
    {
        return $this->categorieProd;
    }

    public function setCategorieProd(?CategorieProd $categorieProd): static
    {
        $this->categorieProd = $categorieProd;

        return $this;
    }

    public function getTags(): ?Tags
    {
        return $this->Tags;
    }

    public function setTags(?Tags $Tags): static
    {
        $this->Tags = $Tags;

        return $this;
    }



}