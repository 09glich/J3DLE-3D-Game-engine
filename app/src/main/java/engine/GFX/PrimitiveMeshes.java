package engine.GFX;

import engine.structs.Color;
import engine.structs.Vector2;
import engine.structs.Vector3;

public final class PrimitiveMeshes {
    private PrimitiveMeshes() {}

    public static Mesh createCube(float size, boolean inwardNormals) {
        Mesh mesh = new Mesh();
        float h = size * 0.5f;

        Vector3[] positions = new Vector3[] {
            // Front
            new Vector3(-h, -h, h), new Vector3(h, -h, h), new Vector3(h, h, h), new Vector3(-h, h, h),
            // Back
            new Vector3(h, -h, -h), new Vector3(-h, -h, -h), new Vector3(-h, h, -h), new Vector3(h, h, -h),
            // Left
            new Vector3(-h, -h, -h), new Vector3(-h, -h, h), new Vector3(-h, h, h), new Vector3(-h, h, -h),
            // Right
            new Vector3(h, -h, h), new Vector3(h, -h, -h), new Vector3(h, h, -h), new Vector3(h, h, h),
            // Top
            new Vector3(-h, h, h), new Vector3(h, h, h), new Vector3(h, h, -h), new Vector3(-h, h, -h),
            // Bottom
            new Vector3(-h, -h, -h), new Vector3(h, -h, -h), new Vector3(h, -h, h), new Vector3(-h, -h, h)
        };

        Vector3[] normals = new Vector3[] {
            new Vector3(0, 0, 1), new Vector3(0, 0, 1), new Vector3(0, 0, 1), new Vector3(0, 0, 1),
            new Vector3(0, 0, -1), new Vector3(0, 0, -1), new Vector3(0, 0, -1), new Vector3(0, 0, -1),
            new Vector3(-1, 0, 0), new Vector3(-1, 0, 0), new Vector3(-1, 0, 0), new Vector3(-1, 0, 0),
            new Vector3(1, 0, 0), new Vector3(1, 0, 0), new Vector3(1, 0, 0), new Vector3(1, 0, 0),
            new Vector3(0, 1, 0), new Vector3(0, 1, 0), new Vector3(0, 1, 0), new Vector3(0, 1, 0),
            new Vector3(0, -1, 0), new Vector3(0, -1, 0), new Vector3(0, -1, 0), new Vector3(0, -1, 0)
        };

        Vector2[] uv = new Vector2[] {
            new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1), new Vector2(0, 1),
            new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1), new Vector2(0, 1),
            new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1), new Vector2(0, 1),
            new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1), new Vector2(0, 1),
            new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1), new Vector2(0, 1),
            new Vector2(0, 0), new Vector2(1, 0), new Vector2(1, 1), new Vector2(0, 1)
        };

        mesh.Verticies = new Mesh.Vertex[24];
        for (int i = 0; i < 24; i++) {
            Mesh.Vertex v = mesh.new Vertex();
            v.position = positions[i];
            v.normal = inwardNormals ? normals[i].Scale(-1f) : normals[i];
            v.uvCoordinate = uv[i];
            v.color = Color.white;
            mesh.Verticies[i] = v;
        }

        int[] tri = new int[] {
            0, 1, 2, 0, 2, 3,
            4, 5, 6, 4, 6, 7,
            8, 9, 10, 8, 10, 11,
            12, 13, 14, 12, 14, 15,
            16, 17, 18, 16, 18, 19,
            20, 21, 22, 20, 22, 23
        };

        mesh.Triangles = toFaces(mesh, tri, inwardNormals);
        mesh.BUILD();
        return mesh;
    }

    public static Mesh createSphere(float radius, int rings, int segments, boolean inwardNormals) {
        Mesh mesh = new Mesh();
        int vertexCount = (rings + 1) * (segments + 1);
        mesh.Verticies = new Mesh.Vertex[vertexCount];

        int vi = 0;
        for (int y = 0; y <= rings; y++) {
            float v = (float) y / (float) rings;
            float phi = (float) (Math.PI * v);
            float sinPhi = (float) Math.sin(phi);
            float cosPhi = (float) Math.cos(phi);

            for (int x = 0; x <= segments; x++) {
                float u = (float) x / (float) segments;
                float theta = (float) (2.0 * Math.PI * u);
                float sinTheta = (float) Math.sin(theta);
                float cosTheta = (float) Math.cos(theta);

                Vector3 normal = new Vector3(cosTheta * sinPhi, cosPhi, sinTheta * sinPhi);
                if (inwardNormals) {
                    normal = normal.Scale(-1f);
                }

                Mesh.Vertex vertex = mesh.new Vertex();
                vertex.position = new Vector3(
                    normal.x * radius,
                    normal.y * radius,
                    normal.z * radius
                );
                vertex.normal = normal;
                vertex.uvCoordinate = new Vector2(u, 1f - v);
                vertex.color = Color.white;
                mesh.Verticies[vi++] = vertex;
            }
        }

        int[] tri = new int[rings * segments * 6];
        int ti = 0;
        for (int y = 0; y < rings; y++) {
            for (int x = 0; x < segments; x++) {
                int i0 = y * (segments + 1) + x;
                int i1 = i0 + 1;
                int i2 = i0 + (segments + 1);
                int i3 = i2 + 1;

                tri[ti++] = i0;
                tri[ti++] = i2;
                tri[ti++] = i1;

                tri[ti++] = i1;
                tri[ti++] = i2;
                tri[ti++] = i3;
            }
        }

        mesh.Triangles = toFaces(mesh, tri, inwardNormals);
        mesh.BUILD();
        return mesh;
    }

    private static Mesh.Face[] toFaces(Mesh mesh, int[] tri, boolean flipWinding) {
        Mesh.Face[] faces = new Mesh.Face[tri.length / 3];
        int t = 0;
        for (int i = 0; i < faces.length; i++) {
            int a = tri[t++];
            int b = tri[t++];
            int c = tri[t++];

            Mesh.Face f = mesh.new Face();
            f.vertexIndex0 = a;
            f.vertexIndex1 = flipWinding ? c : b;
            f.vertexIndex2 = flipWinding ? b : c;
            faces[i] = f;
        }
        return faces;
    }
}
