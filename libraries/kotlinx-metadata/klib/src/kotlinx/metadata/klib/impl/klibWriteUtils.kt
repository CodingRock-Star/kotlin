/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlinx.metadata.klib.impl

import kotlinx.metadata.impl.WriteContext
import kotlinx.metadata.impl.writeAnnotation
import kotlinx.metadata.klib.KlibHeader
import kotlinx.metadata.klib.KlibSourceFile
import kotlinx.metadata.klib.UniqId
import org.jetbrains.kotlin.library.metadata.KlibMetadataProtoBuf
import org.jetbrains.kotlin.serialization.StringTableImpl

internal fun UniqId.writeUniqId(): KlibMetadataProtoBuf.DescriptorUniqId.Builder =
    KlibMetadataProtoBuf.DescriptorUniqId.newBuilder().apply {
        index = this@writeUniqId.index
    }

internal fun KlibHeader.writeHeader(context: WriteContext): KlibMetadataProtoBuf.Header.Builder =
    KlibMetadataProtoBuf.Header.newBuilder().also { proto ->
        val (strings, qualifiedNames) = (context.strings as StringTableImpl).buildProto()
        proto.moduleName = moduleName
        proto.qualifiedNames = qualifiedNames
        proto.strings = strings
        proto.addAllPackageFragmentName(packageFragmentName)
        proto.addAllFile(file.map { it.writeFile().build() })
        proto.addAllAnnotation(annotation.map { it.writeAnnotation(context.strings).build() })
        proto.addAllEmptyPackage(emptyPackage)
    }

internal fun KlibSourceFile.writeFile(): KlibMetadataProtoBuf.File.Builder =
    KlibMetadataProtoBuf.File.newBuilder().also { proto ->
        proto.name = name
    }
