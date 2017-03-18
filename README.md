# Dropbox
临时收容所

```cpp
#include <iostream>
#include <cmath>

using namespace std;

void rotatePoint(float& x, float& y, float angle, float x0=0, float y0=0) {
    float ro = angle * M_PI / 180.0f;

    float rx = (x - x0) * cosf(ro) - (y - y0) * sinf(ro);
    float ry = (x - x0) * sinf(ro) + (y - y0) * cosf(ro);

    x = rx;
    y = ry;
}

class Rect {
    public:
        Rect(int w=0, int h=0):width(w),height(h) {}
        void rotate(float angle) {
            float ltx = - width / 2.0f, lty = - height / 2.0f;
            float rtx =   width / 2.0f, rty = - height / 2.0f;
            float rdx =   width / 2.0f, rdy = height / 2.0f;
            float ldx = - width / 2.0f, ldy = height / 2.0f;

            cout << "lt: " << ltx << ", " << lty << endl;
            cout << "rt: " << rtx << ", " << rty << endl;
            cout << "rd: " << rdx << ", " << rdy << endl;
            cout << "ld: " << ldx << ", " << ldy << endl;

            rotatePoint(ltx, lty, angle);
            rotatePoint(rtx, rty, angle);
            rotatePoint(rdx, rdy, angle);
            rotatePoint(ldx, ldy, angle);

            cout << "After" << endl;
            cout << "lt: " << ltx << ", " << lty << endl;
            cout << "rt: " << rtx << ", " << rty << endl;
            cout << "rd: " << rdx << ", " << rdy << endl;
            cout << "ld: " << ldx << ", " << ldy << endl;


            cout << "求出外切矩形" << endl;
            float minx = min(ltx, min(rtx, min(rdx, ldx)));
            float maxx = max(ltx, max(rtx, max(rdx, ldx)));

            float miny = min(lty, min(rty, min(rdy, ldy)));
            float maxy = max(lty, max(rty, max(rdy, ldy)));

            cout << "lt: " << minx << ", " << miny << endl;
            cout << "rt: " << maxx << ", " << miny << endl;
            cout << "rd: " << maxx << ", " << maxy << endl;
            cout << "ld: " << minx << ", " << maxy << endl;

            cout << "移动外切矩形左上角到 0, 0 位置" << endl;
            float dx = -minx, dy = -miny;
            minx += dx; miny += dy;
            maxx += dx; maxy += dy;

            cout << "lt: " << minx << ", " << miny << endl;
            cout << "rt: " << maxx << ", " << miny << endl;
            cout << "rd: " << maxx << ", " << maxy << endl;
            cout << "ld: " << minx << ", " << maxy << endl;

            cout << "移动旋转后的矩形" << endl;
            ltx += dx; lty += dy;
            rtx += dx; rty += dy;
            rdx += dx; rdy += dy;
            ldx += dx; ldy += dy;

            cout << "Final" << endl;
            cout << "lt: " << ltx << ", " << lty << endl;
            cout << "rt: " << rtx << ", " << rty << endl;
            cout << "rd: " << rdx << ", " << rdy << endl;
            cout << "ld: " << ldx << ", " << ldy << endl;

            float ow = (maxx - minx);
            float oh = (maxy - miny);
            float coord[8];
            coord[0] = ltx / ow; coord[1] = lty / oh;
            coord[2] = rtx / ow; coord[3] = rty / oh;
            coord[4] = ldx / ow; coord[5] = ldy / oh;
            coord[6] = rdx / ow; coord[7] = rdy / oh;

            cout << "[1]: " << coord[0] << "," << coord[1] << endl;
            cout << "[2]: " << coord[2] << "," << coord[3] << endl;
            cout << "[3]: " << coord[4] << "," << coord[5] << endl;
            cout << "[4]: " << coord[6] << "," << coord[7] << endl;
        }
        int width, height;
};

int main(void) {
    std::cout << "run test" << std::endl;

    Rect rect(400, 300);
    rect.rotate(45);
    return 0;
}

```
