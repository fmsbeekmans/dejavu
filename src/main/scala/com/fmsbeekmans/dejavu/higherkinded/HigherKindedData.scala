package com.fmsbeekmans.dejavu.higherkinded

import cats.{ ~>, Functor }
import cats.syntax.functor._
import shapeless._

trait HigherKindedData[F] {
  type G

  def mapK(f: F): G
}

object HigherKindedData {
  type Aux[F, O] = HigherKindedData[F] { type G = O }

  def apply[F, G](
    implicit
    higherKindedData: HigherKindedData.Aux[F, G]
  ): HigherKindedData.Aux[F, G] =
    higherKindedData

  def instance[F, O](fg: F => O): HigherKindedData.Aux[F, O] =
    new HigherKindedData[F] {
      type G = O

      override def mapK(f: F): O = fg(f)
    }

  implicit def higherKindedDataHNil: HigherKindedData.Aux[HNil, HNil] =
    instance[HNil, HNil](_ => HNil)

  implicit def higherKindedDataHigherKindedHead[
    T[_[_]],
    F[_],
    G[_],
    FT <: HList,
    GT <: HList
  ](
    implicit
    higherKindedDataH: Lazy[HigherKindedData.Aux[T[F], T[G]]],
    higherKindedDataT: Lazy[HigherKindedData.Aux[FT, GT]]
  ): HigherKindedData.Aux[T[F] :: FT, T[G] :: GT] =
    instance {
      case h :: t =>
        higherKindedDataH.value.mapK(h) :: higherKindedDataT.value.mapK(t)
    }

  implicit def higherKindedDataHCons[F[_], G[_], H, FT <: HList, GT <: HList](
    implicit
    higherKindedDataT: Lazy[HigherKindedData.Aux[FT, GT]],
    fg: F ~> G
  ): HigherKindedData.Aux[F[H] :: FT, G[H] :: GT] =
    instance { case h :: t => fg(h) :: higherKindedDataT.value.mapK(t) }

  implicit def higherKindedDataGeneric[
    T[_[_]],
    F[_],
    G[_],
    FRepr <: HList,
    GRepr <: HList
  ](
    implicit
    genericTF: Generic.Aux[T[F], FRepr],
    genericTG: Generic.Aux[T[G], GRepr],
    higherKindedDataTFG: Lazy[HigherKindedData.Aux[FRepr, GRepr]]
  ): HigherKindedData.Aux[T[F], T[G]] =
    instance(f =>
      genericTG.from(higherKindedDataTFG.value.mapK(genericTF.to(f)))
    )

  implicit def higherKindedDataHigherKindedHeadGeneric[
    T[_[_]],
    F[_],
    G[_],
    H[_[_]],
    FRepr <: HList,
    GRepr <: HList
  ](
    implicit
    functorF: Functor[F],
    genericTF: Generic.Aux[T[F], F[H[F]] :: FRepr],
    genericTG: Generic.Aux[T[G], G[H[G]] :: GRepr],
    higherKindedDataH: Lazy[HigherKindedData.Aux[H[F], H[G]]],
    higherKindedDataT: Lazy[HigherKindedData.Aux[FRepr, GRepr]],
    fg: F ~> G
  ): HigherKindedData.Aux[T[F], T[G]] =
    instance { f =>
      genericTG
        .from(
          genericTF.to(f) match {
            case h :: t =>
              fg.apply(h.map(higherKindedDataH.value.mapK)) :: higherKindedDataT
                .value
                .mapK(
                  t
                )
          }
        )
    }

  implicit class HigherKindedDataOps[T[_[_]], F[_]](f: T[F]) {
    def mapK[G[_]](
      implicit
      higherKindedDataFG: HigherKindedData.Aux[T[F], T[G]]
    ): T[G] =
      higherKindedDataFG.mapK(f)

  }

}
