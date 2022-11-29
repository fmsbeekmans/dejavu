package com.fmsbeekmans.dejavu

import cats.arrow.FunctionK
import cats.{ ~>, Functor, Id }
import com.fmsbeekmans.dejavu.higherkinded.HigherKindedData
import com.fmsbeekmans.dejavu.higherkinded.HigherKindedData._
import shapeless.{ Generic, HNil }

case class Record[T[_]](
  a: T[String]
)

case class Wrapper[T[_]](
  a: T[Record[T]]
)

object Example extends App {

  implicit val functionKOptionList =
    new FunctionK[Option, List] {
      override def apply[A](fa: Option[A]): List[A] = fa.toList
    }

  val higherKindedDataRecordOptionId
    : HigherKindedData.Aux[Record[Option], Record[List]] =
    HigherKindedData[Record[Option], Record[List]] // implicit resolved

  // could not find implicit value for parameter higherKindedData: HigherKindedData.Aux[Wrapper[Option],Wrapper[List]]
  val higherKindedDataWrapperOptionList
    : HigherKindedData.Aux[Wrapper[Option], Wrapper[List]] =
    HigherKindedData[Wrapper[Option], Wrapper[List]]

  // implicit not found, missing HigherKindedData.Aux[Record[Option], Record[List]], L39 shows it is derivable
  // could not find Lazy implicit value of type HigherKindedData.Aux[Record[Option],Record[List]]
  // [error]       higherKindedDataHigherKindedHeadGeneric[
  val higherKindedDataWrapperOptionListExplicit1 =
    HigherKindedData[Wrapper[Option], Wrapper[List]](
      higherKindedDataHigherKindedHeadGeneric[
        Wrapper,
        Option,
        List,
        Record,
        HNil,
        HNil
      ]
    )

  // works when the missing implicit from above is added explicitly
  val higherKindedDataWrapperOptionListExplicit2 = {
    HigherKindedData[Wrapper[Option], Wrapper[List]](
      higherKindedDataHigherKindedHeadGeneric[
        Wrapper,
        Option,
        List,
        Record,
        HNil,
        HNil
      ](
        Functor[Option],
        Generic[Wrapper[Option]],
        Generic[Wrapper[List]],
        HigherKindedData[Record[Option], Record[List]],
        higherKindedDataHNil,
        implicitly[Option ~> List]
      )
    )
  }

}
