<?php

namespace App\Controller;
use App\Entity\Produit;
use App\Repository\ProduitRepository;
use App\Entity\Tags;
use App\Form\TagsType;
use App\Repository\TagsRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Knp\Component\Pager\PaginatorInterface;
use Doctrine\Persistence\ManagerRegistry;

#[Route('/tags')]
class TagsController extends AbstractController
{
    #[Route('/', name: 'app_tags_index', methods: ['GET'])]
    public function index(TagsRepository $tagsRepository,PaginatorInterface $paginator,Request $request): Response
    {

        return $this->render('tags/index.html.twig', [
            'tags' => $tagsRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_tags_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $tag = new Tags();
        $form = $this->createForm(TagsType::class, $tag);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($tag);
            $entityManager->flush();
            flash()->addSuccess('Tag Added Successfully');
            return $this->redirectToRoute('app_tags_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('tags/new.html.twig', [
            'tag' => $tag,
            'form' => $form,
        ]);
    }

    #[Route('/{id_tag}/edit', name: 'app_tags_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Tags $tag, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(TagsType::class, $tag);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            flash()->addSuccess('Tag Modified Successfully');
            return $this->redirectToRoute('app_tags_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('tags/edit.html.twig', [
            'tag' => $tag,
            'form' => $form,
        ]);
    }
    #[Route('/dqltags', name: 'dqltags', methods: ['POST'])]//recherche avec dql
    public function dql(EntityManagerInterface $em, Request $request, ProduitRepository $repo):Response
    {
        $result=$repo->findAll();
        $req=$em->createQuery("select d from App\Entity\Tags d where d.Theme=:n OR d.Genre =:n OR d.id_tag =:n");
        if($request->isMethod('post'))
        {
            $value=$request->get('test');
            $req->setParameter('n',$value);

            $result=$req->getResult();

        }

        return $this->render('tags/index.html.twig',[
            'tags'=>$result,
        ]);

    }

    #[Route('/{id_tag}', name: 'app_tags_delete')]
    public function delete(TagsRepository   $repo,$id_tag, ManagerRegistry $mr): Response
    {

        $tag=$repo->find($id_tag);
        $em=$mr->getManager();
        $em->remove($tag);
        $em->flush();
        flash()->addSuccess('Tag Deleted');

        return $this->redirectToRoute('app_tags_index');
    }
}