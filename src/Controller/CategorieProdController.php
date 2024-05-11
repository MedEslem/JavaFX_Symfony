<?php

namespace App\Controller;

use App\Entity\Produit;
use App\Repository\ProduitRepository;
use App\Entity\CategorieProd;
use App\Form\CategorieProdType;
use App\Repository\CategorieProdRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Doctrine\Persistence\ManagerRegistry;
#[Route('/categorie/prod')]
class CategorieProdController extends AbstractController
{
    #[Route('/', name: 'app_categorie_prod_index', methods: ['GET'])]
    public function index(CategorieProdRepository $categorieProdRepository): Response
    {
        return $this->render('categorie_prod/index.html.twig', [
            'categorie_prods' => $categorieProdRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_categorie_prod_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $categorieProd = new CategorieProd();
        $form = $this->createForm(CategorieProdType::class, $categorieProd);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get('image_categ')->getData();
            if ($file) {
                // Generate a unique name for the file before saving it
                $fileName = md5(uniqid()).'.'.$file->guessExtension();

                // Move the file to the directory where brochures are stored
                $targetDirectory = $this->getParameter('kernel.project_dir') . '/public';
                $file->move(
                    $targetDirectory,
                    $fileName
                );
                $categorieProd->setImageCateg($fileName);
            }
            $entityManager->persist($categorieProd);
            $entityManager->flush();
            flash()->addSuccess('Category Added Successfully');

            return $this->redirectToRoute('app_categorie_prod_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('categorie_prod/new.html.twig', [
            'categorie_prod' => $categorieProd,
            'form' => $form,
        ]);
    }

    #[Route('/{id_categProd}/edit', name: 'app_categorie_prod_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, CategorieProd $categorieProd, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(CategorieProdType::class, $categorieProd);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get('image_categ')->getData();
            if ($file) {
                // Generate a unique name for the file before saving it
                $fileName = md5(uniqid()).'.'.$file->guessExtension();

                // Move the file to the directory where brochures are stored
                $targetDirectory = $this->getParameter('kernel.project_dir') . '/public';
                $file->move(
                    $targetDirectory,
                    $fileName
                );
                $categorieProd->setImageCateg($fileName);
            }
            $entityManager->flush();
            flash()->addSuccess('Category Modified Successfully');

            return $this->redirectToRoute('app_categorie_prod_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('categorie_prod/edit.html.twig', [
            'categorie_prod' => $categorieProd,
            'form' => $form,
        ]);
    }

    #[Route('/{id_categProd}', name: 'app_categorie_prod_delete')]
    public function delete(CategorieProdRepository   $repo,$id_categProd, ManagerRegistry $mr): Response
    {

        $categorie=$repo->find($id_categProd);
        $em=$mr->getManager();
        $em->remove($categorie);
        $em->flush();
        flash()->addSuccess('Category Deleted Successfully');

        return $this->redirectToRoute('app_categorie_prod_index');
    }
}